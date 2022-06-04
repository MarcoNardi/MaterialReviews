package com.example.materialreviews

import android.content.Context
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.material.contentColorFor

fun getImageBitmap(imageUri: String, context: Context): Bitmap {
    val uri = Uri.parse(imageUri)
    val contentResolver=context.contentResolver
    //nel caso siano le immagini selezionate dalla galleria bisogna controllare per i permessi
    if(uri.scheme=="content"){
        //assegno il permesso al contentResolver
        contentResolver.takePersistableUriPermission(uri, FLAG_GRANT_READ_URI_PERMISSION )
    }
    val imageData: Bitmap
    if (Build.VERSION.SDK_INT < 28) {
        imageData = MediaStore.Images.Media.getBitmap(contentResolver, uri)

    } else {
        val dataSource =
            ImageDecoder.createSource(contentResolver, uri)

        imageData = ImageDecoder.decodeBitmap(dataSource)
    }
    return imageData
}