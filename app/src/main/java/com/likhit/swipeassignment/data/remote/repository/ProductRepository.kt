package com.likhit.swipeassignment.data.remote.repository

import com.likhit.swipeassignment.data.remote.ProductApi
import com.likhit.swipeassignment.data.remote.model.Product
import com.likhit.swipeassignment.utils.Result
import com.likhit.swipeassignment.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProductRepository(
    private val api: ProductApi
) {
    fun getProducts() = safeApiCall {
        api.getProducts()
    }

    fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        file: File? = null
    ) = safeApiCall {
        api.addProduct(
            productName = productName.toRequestBody("text/plain".toMediaTypeOrNull()),
            productType = productType.toRequestBody("text/plain".toMediaTypeOrNull()),
            price = price.toRequestBody("text/plain".toMediaTypeOrNull()),
            tax = tax.toRequestBody("text/plain".toMediaTypeOrNull()),
            files = file?.let {
                listOf(
                    MultipartBody.Part.createFormData(
                        name = "files[]",
                        filename = it.name,
                        body = it.asRequestBody(
                            "image/*".toMediaTypeOrNull()
                        )
                    )
                )
            }
        )
    }

    suspend fun searchProduct(
        allProducts: List<Product>,
        query: String
    ): Flow<Result<List<Product>>> {
        return flow {
            try {
                emit(Result.Loading())
                val filteredProducts = allProducts.filter { product ->
                    product.product_name.contains(query, true) ||
                            product.product_type.contains(query, true)
                }
                emit(Result.Success(filteredProducts))
            }catch (e: Exception){
                emit(Result.Error(message = e.message))
            }
        }
    }
}