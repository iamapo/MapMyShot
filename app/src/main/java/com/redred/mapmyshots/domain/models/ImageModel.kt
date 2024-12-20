package com.redred.mapmyshots.domain.models

data class ImageModel(
    val uri: String,
    val name: String,
    val dateCreated: Long,
    var hasLocation: Boolean
)