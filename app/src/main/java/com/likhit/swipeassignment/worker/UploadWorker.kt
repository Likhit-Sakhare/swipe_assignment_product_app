package com.likhit.swipeassignment.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.likhit.swipeassignment.data.local.model.LocalProduct
import com.likhit.swipeassignment.data.local.repository.LocalProductRepository
import com.likhit.swipeassignment.data.remote.repository.ProductRepository
import com.likhit.swipeassignment.utils.isNetworkAvailable
import com.likhit.swipeassignment.presentation.utils.showNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
) : Worker(appContext, workerParameters), KoinComponent {

    //WorkManager creates workers internally, so constructor injection won't work, that's why we use
    //koinComponent and provide dependencies manually
    private val productRepository: ProductRepository by inject()
    private val localProductRepository: LocalProductRepository by inject()

    override fun doWork(): Result {
        if (!isNetworkAvailable(applicationContext)) {
            return Result.retry() // this result is from Kotlin
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = localProductRepository.getAllProducts()
                if (products.isNotEmpty()) {
                    products.forEach { localProduct ->
                        val success = addLocalProductToRemote(localProduct)
                        if(!success){
                            return@launch
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("UploadWorker", "Error: ${e.message}")
                return@launch
            }
        }
        return Result.success()
    }

    private suspend fun addLocalProductToRemote(localProduct: LocalProduct): Boolean {
        val image = localProduct.image?.let {
            val timeStamp = SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
            ).format(Date())
            val filePath = "$timeStamp.jpg"
            val file = File(applicationContext.cacheDir, filePath)
            file.writeBytes(it)
            file
        }
        var uploadSuccess = false
        productRepository.addProduct(
            productName = localProduct.product_name,
            productType = localProduct.product_type,
            price = localProduct.price.toString(),
            tax = localProduct.tax.toString(),
            file = image
        ).collect { result ->
            when(result){
                is com.likhit.swipeassignment.utils.Result.Success -> {
                    localProductRepository.deleteProduct(localProduct)
                    uploadSuccess = true
                    showNotification(
                        applicationContext,
                        "Congrats",
                        "Product added successfully and deleted from the local storage"
                    )
                }
                is com.likhit.swipeassignment.utils.Result.Error -> uploadSuccess = false
                is com.likhit.swipeassignment.utils.Result.Loading -> {}
            }
        }
        return uploadSuccess
    }
}