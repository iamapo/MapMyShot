package com.redred.mapmyshots.domain.usecase

import com.redred.mapmyshots.data.repository.ImageRepository
import com.redred.mapmyshots.domain.models.ImageModel

class GetImagesWithLocationWithinTimeUseCase(
    private val imageRepository: ImageRepository
) {

    suspend operator fun invoke(timestamp: Long): List<ImageModel> {
        return imageRepository.getImagesWithLocationWithinTime(
            timestamp = timestamp
        )
    }
}