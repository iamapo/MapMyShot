package com.redred.mapmyshots.data.datasource

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import com.redred.mapmyshots.domain.models.ImageModel
import java.io.IOException

class MediaStoreImageDataSource(private val context: Context) : ImageDataSource {
    override fun getImages(): List<ImageModel> {
        val images = mutableListOf<ImageModel>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_TAKEN
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            val dateAddedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
            val dateTakenColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateCreated = cursor.getLong(dateTakenColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                val hasLocation = try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val exif = inputStream?.use { ExifInterface(it) }
                    val lat = exif?.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                    val lon = exif?.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                    lat != null && lon != null
                } catch (e: IOException) {
                    false
                }

                images.add(
                    ImageModel(
                        uri = uri.toString(),
                        hasLocation = hasLocation,
                        name = name,
                        dateCreated = dateCreated
                    )
                )
            }
        }
        return images
    }
}