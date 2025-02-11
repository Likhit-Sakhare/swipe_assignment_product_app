package com.likhit.swipeassignment.di

import com.likhit.swipeassignment.presentation.product_add.AddProductViewModel
import com.likhit.swipeassignment.presentation.product_list.ProductListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        ProductListViewModel(androidContext(), get())
    }
    viewModel {
        AddProductViewModel(androidContext(), get(), get())
    }
}