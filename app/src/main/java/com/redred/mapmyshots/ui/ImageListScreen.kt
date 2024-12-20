package com.redred.mapmyshots.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.redred.mapmyshots.domain.models.ImageModel
import com.redred.mapmyshots.ui.viewmodel.ImageViewModel
import org.koin.androidx.compose.getViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ImageListScreen(viewModel: ImageViewModel = getViewModel()) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "imageList") {
        composable("imageList") {
            ImageListScreenContent(navController = navController, viewModel = viewModel)
        }
        composable("imageDetail/{imageUri}") { backStackEntry ->
            val encodedUri = backStackEntry.arguments?.getString("imageUri")
            val imageUri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString())
            val image = viewModel.getImageByUri(imageUri) // Fetch the image using the URI
            image?.let { ImageDetailScreen(image = it) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageListScreenContent(navController: NavController, viewModel: ImageViewModel) {
    val images = viewModel.imagesWithoutLocation.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.fetchImagesWithoutLocation()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Bilder ohne Standort") },
                modifier = Modifier
                    .padding(horizontal = 14.dp)
            )
        }
    ) { padding ->
        val imagePairs = images.chunked(2)

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(imagePairs) { pair ->
                ImageRow(imagePair = pair, navController = navController)
            }
        }
    }
}

@Composable
fun ImageRow(imagePair: List<ImageModel>, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ImageItem(image = imagePair[0], onClick = {
            val encodedUri = URLEncoder.encode(imagePair[0].uri, StandardCharsets.UTF_8.toString())
            navController.navigate("imageDetail/$encodedUri")
        })

        if (imagePair.size > 1) {
            ImageItem(image = imagePair[1], onClick = {
                val encodedUri = URLEncoder.encode(imagePair[1].uri, StandardCharsets.UTF_8.toString())
                navController.navigate("imageDetail/$encodedUri")
            })
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ImageItem(image: ImageModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = rememberAsyncImagePainter(image.uri),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .background(Color.Black),
            contentScale = ContentScale.Crop
        )
        Text(
            text = formatDate(image.dateCreated),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}






