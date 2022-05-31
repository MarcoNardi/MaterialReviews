package com.example.materialreviews

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun getImageBitmap(imageUri: String, context: Context): Bitmap {
    val uri = Uri.parse(imageUri)
    val imageData: Bitmap
    if (Build.VERSION.SDK_INT < 28) {
        imageData = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

    } else {
        val dataSource =
            ImageDecoder.createSource(context.contentResolver, uri)

        imageData = ImageDecoder.decodeBitmap(dataSource)
    }
    return imageData
}