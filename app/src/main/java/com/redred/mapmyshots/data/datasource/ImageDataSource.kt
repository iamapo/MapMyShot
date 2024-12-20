package com.redred.mapmyshots.data.datasource

import com.redred.mapmyshots.domain.models.ImageModel

interface ImageDataSource {
    fun getImages(): List<ImageModel>
}
