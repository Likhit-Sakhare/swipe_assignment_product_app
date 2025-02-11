package com.likhit.swipeassignment.di

import androidx.room.Room
import com.likhit.swipeassignment.data.local.ProductDatabase
import com.likhit.swipeassignment.data.local.repository.LocalProductRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = ProductDatabase::class.java,
            name = "product_database"
        ).build()
    }

    single {
        get<ProductDatabase>().productDao()
    }

    single {
        LocalProductRepository(get())
    }
}