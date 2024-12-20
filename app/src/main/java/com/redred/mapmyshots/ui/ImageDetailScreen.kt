package com.redred.mapmyshots.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.redred.mapmyshots.domain.models.ImageModel
import com.redred.mapmyshots.ui.viewmodel.ImageViewModel
import org.koin.androidx.compose.getViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailScreen(
    image: ImageModel,
    viewModel: ImageViewModel = getViewModel()
) {
    val relatedImages = viewModel.imagesWithLocation.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.getImagesTakenWithinHour(image.dateCreated)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Image Details") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(25.dp)) {
            Image(
                painter = rememberAsyncImagePainter(image.uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = "Date Created: ${formatDate(image.dateCreated)}")
            Text(text = if (image.hasLocation) "Location available" else "No location")

            LazyColumn {
                items(relatedImages) { relatedImage ->
                    ImageItem(image = relatedImage, onClick = {})
                }
            }
        }
    }
}
