package com.redred.mapmyshots.di

import com.redred.mapmyshots.data.repository.ImageRepository
import com.redred.mapmyshots.data.file.DefaultImageFileHandler
import com.redred.mapmyshots.data.datasource.ImageDataSource
import com.redred.mapmyshots.data.file.ImageFileHandler
import com.redred.mapmyshots.data.datasource.MediaStoreImageDataSource
import com.redred.mapmyshots.domain.usecase.GetImagesWithLocationWithinTimeUseCase
import com.redred.mapmyshots.domain.usecase.GetImagesWithoutLocationUseCase
import com.redred.mapmyshots.ui.viewmodel.ImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ImageDataSource> { MediaStoreImageDataSource(get()) }

    single { GetImagesWithoutLocationUseCase(get()) }

    single { GetImagesWithLocationWithinTimeUseCase(get()) }

    single<ImageFileHandler> { DefaultImageFileHandler(get()) }

    single { ImageRepository(get(), get()) }

    viewModel { ImageViewModel(get(), get()) }
}
