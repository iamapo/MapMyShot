package com.redred.mapmyshots.domain.usecase

import com.redred.mapmyshots.data.repository.ImageRepository
import com.redred.mapmyshots.domain.models.ImageModel

class GetImagesWithoutLocationUseCase(
    private val imageRepository: ImageRepository
) {

    suspend operator fun invoke(): List<ImageModel> {
        return imageRepository.getImagesWithoutLocation()
    }
}