package com.redred.mapmyshots.data.repository

import android.net.Uri
import com.redred.mapmyshots.data.datasource.ImageDataSource
import com.redred.mapmyshots.data.file.ImageFileHandler
import com.redred.mapmyshots.domain.models.ImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class ImageRepository(
    private val imageDataSource: ImageDataSource,
    private val imageFileHandler: ImageFileHandler
) {

    open suspend fun getImagesWithoutLocation(): List<ImageModel> {
        return withContext(Dispatchers.IO) {
            val images = imageDataSource.getImages()
            images.filterNot { image ->
                val filePath = imageFileHandler.getFilePathFromUri(Uri.parse(image.uri))
                filePath?.let { path ->
                    imageFileHandler.hasGpsInfo(path)
                } ?: false
            }
        }
    }

    open suspend fun getImagesWithLocationWithinTime(timestamp: Long): List<ImageModel> {
        return withContext(Dispatchers.IO) {
            val images = imageDataSource.getImages()
            val oneHourInMillis = 60 * 60 * 1000
            images.filter { image ->
                val filePath = imageFileHandler.getFilePathFromUri(Uri.parse(image.uri))
                val hasLocation = filePath?.let { path ->
                    imageFileHandler.hasGpsInfo(path)
                } ?: false
                hasLocation && image.dateCreated in (timestamp - oneHourInMillis)..(timestamp + oneHourInMillis)
            }
        }
    }
}

