package com.likhit.swipeassignment.presentation.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import java.io.File

fun getFileFromUri(
    contentResolver: ContentResolver,
    uri: Uri
): File?{
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(
        uri,
        filePathColumn,
        null,
        null,
        null
    )
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
    val filePath = cursor?.getString(columnIndex!!)
    cursor?.close()
    return filePath?.let {
        File(it)
    }
}