package com.likhit.swipeassignment.presentation.product_add

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.likhit.swipeassignment.data.local.model.LocalProduct
import com.likhit.swipeassignment.data.local.repository.LocalProductRepository
import com.likhit.swipeassignment.data.remote.repository.ProductRepository
import com.likhit.swipeassignment.presentation.utils.UIState
import com.likhit.swipeassignment.presentation.utils.isNetworkAvailable
import com.likhit.swipeassignment.presentation.utils.showNotification
import com.likhit.swipeassignment.presentation.utils.showToast
import com.likhit.swipeassignment.utils.Result
import com.likhit.swipeassignment.worker.UploadWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class AddProductViewModel(
    private val appContext: Context,
    private val productRepository: ProductRepository,
    private val localProductRepository: LocalProductRepository
): ViewModel() {

    val productTypes = listOf(
        "Select",
        "General",
        "Electronics",
        "Food",
        "Clothing",
        "Accessories",
        "Cosmetics",
        "Book"
    )

    private val _isDropDownExpanded = MutableStateFlow(false)
    val isDropDownExpanded = _isDropDownExpanded.asStateFlow()

    private val _selectedProductType = MutableStateFlow(productTypes[0])
    val selectedProductType = _selectedProductType.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _price = MutableStateFlow("")
    val price = _price.asStateFlow()

    private val _tax = MutableStateFlow("")
    val tax = _tax.asStateFlow()

    private val _image = MutableStateFlow<File?>(null)
    val image = _image.asStateFlow()

    private val _uiState = MutableStateFlow(UIState.NOTHING)
    val uiState = _uiState.asStateFlow()

    fun toggleDropDown(){
        _isDropDownExpanded.value = !_isDropDownExpanded.value
    }

    fun setSelectedProductType(type: String){
        _selectedProductType.value = type
    }

    fun setName(name: String){
        _name.value = name
    }

    fun setPrice(price: String){
        _price.value = price
    }

    fun setTax(tax: String){
        _tax.value = tax
    }

    fun setImage(file: File?){
        _image.value = file
    }

    fun addProduct(
        goBack: () -> Unit
    ){
        val name = _name.value
        val productType = _selectedProductType.value
        val price = _price.value.toDouble().toString()
        val tax = _tax.value.toDouble().toString()
        val file = _image.value

        if(name.trim().isEmpty() || price.toDouble() == "0.0".toDouble()
            || tax.toDouble() == "0.0".toDouble()){
            showToast(appContext, "Please fill all the fields")
            return
        }

        viewModelScope.launch {
            if(!isNetworkAvailable(appContext)){
                localProductRepository.upsertProduct(
                    LocalProduct(
                        product_name = name,
                        product_type = productType,
                        price = price.toDouble(),
                        tax = tax.toDouble(),
                        image = file?.readBytes()
                    )
                )
                scheduleProductSync(context = appContext)
                showToast(appContext, "Product added locally")
                goBack()
            }else{
                productRepository.addProduct(
                    productName = name,
                    productType = productType,
                    price = price,
                    tax = tax,
                    file = file
                ).collect { result ->
                    when(result){
                        is Result.Success -> {
                            _uiState.value = UIState.SUCCESS
                            showToast(
                                appContext,
                                "Product added successfully")
                            showNotification(
                                appContext,
                                "Congrats!",
                                "Your product gets added to the product list successfully"
                            )
                            goBack()
                        }
                        is Result.Error -> {
                            result.message?.let {
                                Log.e("AddProductError", it)
                                showToast(
                                    appContext,
                                    it
                                )
                            }
                            _uiState.update { UIState.ERROR }
                        }
                        is Result.Loading -> {
                            _uiState.update { UIState.LOADING }
                        }
                    }
                }
            }
        }
    }

    private fun scheduleProductSync(context: Context){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}