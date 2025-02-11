package com.likhit.swipeassignment.data.local.repository

import com.likhit.swipeassignment.data.local.ProductDao
import com.likhit.swipeassignment.data.local.model.LocalProduct

class LocalProductRepository(
    private val productDao: ProductDao
) {
    suspend fun getAllProducts() = productDao.getAllProducts()

    suspend fun upsertProduct(product: LocalProduct) = productDao.upsertProduct(product)

    suspend fun deleteProduct(product: LocalProduct) = productDao.deleteProduct(product)
}