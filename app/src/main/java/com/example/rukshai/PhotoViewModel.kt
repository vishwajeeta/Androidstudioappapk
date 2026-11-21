package com.example.rukshai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application) :
    AndroidViewModel(application) {

    private val photoDao = PhotoDatabase
        .getDatabase(application)
        .photoDao()

    val photos = photoDao.getAllPhotos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun savePhoto(
        imagePath: String,
        note: String = "",
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                photoDao.insertPhoto(
                    PhotoEntity(
                        imagePath = imagePath,
                        note = note
                    )
                )

                onComplete(true)

            } catch (exception: Exception) {
                onComplete(false)
            }
        }
    }

    fun deletePhoto(photo: PhotoEntity) {
        viewModelScope.launch {
            photoDao.deletePhoto(photo)
        }
    }
}