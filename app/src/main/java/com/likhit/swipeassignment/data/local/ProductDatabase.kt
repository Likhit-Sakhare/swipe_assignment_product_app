package com.likhit.swipeassignment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.likhit.swipeassignment.data.local.model.LocalProduct

@Database(
    entities = [LocalProduct::class],
    version = 1
)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
}