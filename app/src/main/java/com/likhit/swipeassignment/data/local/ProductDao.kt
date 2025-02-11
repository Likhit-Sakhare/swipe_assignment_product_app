package com.likhit.swipeassignment.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.likhit.swipeassignment.data.local.model.LocalProduct

@Dao
interface ProductDao {

    @Query("SELECT * FROM product_table")
    suspend fun getAllProducts(): List<LocalProduct>

    @Upsert
    suspend fun upsertProduct(product: LocalProduct)

    @Delete
    suspend fun deleteProduct(product: LocalProduct)
}