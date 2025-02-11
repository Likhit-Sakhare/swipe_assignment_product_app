package com.likhit.swipeassignment.presentation.product_list

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likhit.swipeassignment.data.remote.model.Product
import com.likhit.swipeassignment.data.remote.repository.ProductRepository
import com.likhit.swipeassignment.presentation.utils.UIState
import com.likhit.swipeassignment.presentation.utils.isNetworkAvailable
import com.likhit.swipeassignment.presentation.utils.showToast
import com.likhit.swipeassignment.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val appContext: Context,
    private val productRepository: ProductRepository
): ViewModel(){
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private var allProducts: List<Product> = emptyList()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow(UIState.NOTHING)
    val uiState = _uiState.asStateFlow()

    init {
        getProducts()
    }

    fun refreshProducts(){
        _products.update { emptyList() }
        getProducts(isRefreshing = true)
    }

    private fun getProducts(isRefreshing: Boolean = false){
        if(!isNetworkAvailable(appContext)){
            showToast(appContext, "No internet connection")
            return
        }
        viewModelScope.launch {
            productRepository.getProducts().collect { result ->
                when(result){
                    is Result.Success -> {
                        result.data.also { response ->
                            val responseBody = response?.body()
                            if(responseBody == null){
                                Log.e("GetProductError", "Response body is null")
                                return@collect
                            }
                            allProducts = responseBody
                            _products.update { responseBody }
                            _uiState.value = UIState.SUCCESS
                        }
                    }
                    is Result.Error -> {
                        result.message?.let {
                            Log.e("GetProductsError", it)
                            showToast(appContext, it)
                            _uiState.value = UIState.ERROR
                        }
                    }
                    is Result.Loading -> {
                        _uiState.value = if(isRefreshing) UIState.REFRESHING else UIState.LOADING
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String){
        _searchQuery.value = query

        if(query.isEmpty()){
            _products.update { allProducts }
        }else{
            searchProducts(query.trim())
        }
    }

    private fun searchProducts(query: String){
        viewModelScope.launch {
            productRepository.searchProduct(allProducts, query).collect { result ->
                when(result){
                    is Result.Success -> {
                        result.data?.also { filteredProducts ->
                            _products.update { filteredProducts }
                            _uiState.value = UIState.SUCCESS
                        }
                    }
                    is Result.Error -> {
                        result.message?.let {
                            Log.e("SearchProduct", it)
                            showToast(appContext, it)
                            _uiState.value = UIState.ERROR
                        }
                    }
                    is Result.Loading -> {
                        _uiState.value = UIState.LOADING
                    }
                }
            }
        }
    }
}