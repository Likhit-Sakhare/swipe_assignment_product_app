package com.likhit.swipeassignment.di

import com.likhit.swipeassignment.data.remote.ProductApi
import com.likhit.swipeassignment.data.remote.repository.ProductRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(ProductApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ProductApi> {
        get<Retrofit>().create(ProductApi::class.java)
    }

    single {
        ProductRepository(get())
    }
}