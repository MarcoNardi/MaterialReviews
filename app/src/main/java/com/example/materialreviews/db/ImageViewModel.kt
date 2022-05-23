package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ImageViewModel(private val imageDao: ImageDao) : ViewModel() {
    private fun insertImage(image: ImageEntity) {
        viewModelScope.launch{
            imageDao.insert(image)
        }
    }

    private fun createImageEntry(uri: String, rid: Int): ImageEntity{
        return ImageEntity(uri, rid )
    }

    fun addImage(uri: String, rid: Int){
        insertImage(createImageEntry(uri, rid))
    }



}

class ImageViewModelFactory(private val imageDao: ImageDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageViewModel(imageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}