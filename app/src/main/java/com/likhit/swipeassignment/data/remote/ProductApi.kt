package com.likhit.swipeassignment.data.remote

import com.likhit.swipeassignment.data.remote.model.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductApi {

    @GET("get")
    suspend fun getProducts(): Response<List<Product>>

    @Multipart
    @POST("add")
    suspend fun addProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part files: List<MultipartBody.Part>? = null
    ): Response<Any>

    companion object{
        const val BASE_URL = "https://app.getswipe.in/api/public/"
    }
}