package com.likhit.swipeassignment.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class LocalProduct(
    @ColumnInfo(name = "product_name")
    var product_name: String,

    @ColumnInfo(name = "product_type")
    var product_type: String,

    @ColumnInfo(name = "price")
    var price: Double,

    @ColumnInfo(name = "tax")
    var tax: Double,

    @ColumnInfo(
        name = "image",
        typeAffinity = ColumnInfo.BLOB
    )
    var image: ByteArray? = null,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocalProduct

        if (product_name != other.product_name) return false
        if (product_type != other.product_type) return false
        if (price != other.price) return false
        if (tax != other.tax) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = product_name.hashCode()
        result = 31 * result + product_type.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + tax.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + id
        return result
    }
}