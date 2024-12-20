package com.redred.mapmyshots.data.file

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface

class DefaultImageFileHandler(private val context: Context) : ImageFileHandler {
    override fun getFilePathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        return context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.getString(columnIndex)
            } else null
        }
    }

    override fun hasGpsInfo(filePath: String): Boolean {
        return try {
            val exifInterface = ExifInterface(filePath)
            exifInterface.latLong != null
        } catch (e: Exception) {
            false
        }
    }
}