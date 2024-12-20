package com.redred.mapmyshots

import com.redred.mapmyshots.data.datasource.ImageDataSource
import com.redred.mapmyshots.data.file.ImageFileHandler
import com.redred.mapmyshots.data.repository.ImageRepository
import com.redred.mapmyshots.domain.models.ImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ImageRepositoryTest {

    private lateinit var imageDataSource: ImageDataSource
    private lateinit var imageFileHandler: ImageFileHandler
    private lateinit var imageRepository: ImageRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        imageDataSource = mock(ImageDataSource::class.java)
        imageFileHandler = mock(ImageFileHandler::class.java)
        imageRepository = ImageRepository(imageDataSource, imageFileHandler)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getImagesWithoutLocation should return images without GPS info`() = runBlocking {
        // Arrange
        val images = listOf(
            ImageModel("1", true),
            ImageModel("2", true)
        )
        `when`(imageDataSource.getImages()).thenReturn(images)
        `when`(imageFileHandler.getFilePathFromUri(Uri.parse("uri1"))).thenReturn("path1")
        `when`(imageFileHandler.getFilePathFromUri(Uri.parse("uri2"))).thenReturn("path2")
        `when`(imageFileHandler.hasGpsInfo("path1")).thenReturn(false)
        `when`(imageFileHandler.hasGpsInfo("path2")).thenReturn(true)

        // Act
        val result = imageRepository.getImagesWithoutLocation()

        // Assert
        assertEquals(listOf(images[0]), result)
        verify(imageDataSource).getImages()
        verify(imageFileHandler).getFilePathFromUri(Uri.parse("uri1"))
        verify(imageFileHandler).getFilePathFromUri(Uri.parse("uri2"))
        verify(imageFileHandler).hasGpsInfo("path1")
        verify(imageFileHandler).hasGpsInfo("path2")
    }

    @Test
    fun `getImagesWithoutLocation should return empty list when all images have GPS info`() = runBlocking {
        // Arrange
        val images = listOf(
            ImageModel("1", "uri1"),
            ImageModel("2", "uri2")
        )
        `when`(imageDataSource.getImages()).thenReturn(images)
        `when`(imageFileHandler.getFilePathFromUri(Uri.parse("uri1"))).thenReturn("path1")
        `when`(imageFileHandler.getFilePathFromUri(Uri.parse("uri2"))).thenReturn("path2")
        `when`(imageFileHandler.hasGpsInfo("path1")).thenReturn(true)
        `when`(imageFileHandler.hasGpsInfo("path2")).thenReturn(true)

        // Act
        val result = imageRepository.getImagesWithoutLocation()

        // Assert
        assertEquals(emptyList<ImageModel>(), result)
        verify(imageDataSource).getImages()
        verify(imageFileHandler).getFilePathFromUri(Uri.parse("uri1"))
        verify(imageFileHandler).getFilePathFromUri(Uri.parse("uri2"))
        verify(imageFileHandler).hasGpsInfo("path1")
        verify(imageFileHandler).hasGpsInfo("path2")
    }
}
