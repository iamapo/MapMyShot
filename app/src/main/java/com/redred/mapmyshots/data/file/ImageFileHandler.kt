package com.redred.mapmyshots.data.file

import android.net.Uri

interface ImageFileHandler {
    fun getFilePathFromUri(uri: Uri): String?
    fun hasGpsInfo(filePath: String): Boolean
}