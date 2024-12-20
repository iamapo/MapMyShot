package com.redred.mapmyshots.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redred.mapmyshots.domain.models.ImageModel
import com.redred.mapmyshots.domain.usecase.GetImagesWithLocationWithinTimeUseCase
import com.redred.mapmyshots.domain.usecase.GetImagesWithoutLocationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImageViewModel(
    private val getImagesWithoutLocationUseCase: GetImagesWithoutLocationUseCase,
    private val getImagesWithLocationWithinTimeUseCase: GetImagesWithLocationWithinTimeUseCase
) : ViewModel() {

    private val _imagesWithoutLocation = MutableStateFlow<List<ImageModel>>(emptyList())
    val imagesWithoutLocation: StateFlow<List<ImageModel>> get() = _imagesWithoutLocation

    private val _imagesWithLocation = MutableStateFlow<List<ImageModel>>(emptyList())
    val imagesWithLocation: StateFlow<List<ImageModel>> get() = _imagesWithLocation

    fun fetchImagesWithoutLocation() {
        viewModelScope.launch {
            val images = getImagesWithoutLocationUseCase()
            Log.d("ImageViewModel", "Images: $images")
            _imagesWithoutLocation.value = images
        }
    }

    fun getImagesTakenWithinHour(timestamp: Long) {
        viewModelScope.launch {
            val images = getImagesWithLocationWithinTimeUseCase(timestamp)
            Log.d("ImageViewModel", "Images: $images")
            _imagesWithLocation.value = images
        }

    }

    fun getImageByUri(uri: String?): ImageModel? {
        return _imagesWithoutLocation.value.find { it.uri == uri }
    }
}