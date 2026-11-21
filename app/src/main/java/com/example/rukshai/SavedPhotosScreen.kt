package com.example.rukshai

import android.net.Uri
import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import java.io.File

@Composable
fun SavedPhotosScreen(
    photoViewModel: PhotoViewModel,
    onBack: () -> Unit
) {

    val photos by photoViewModel.photos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(onClick = onBack) {
                Text("← Back")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Saved Photos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${photos.size} photos saved",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (photos.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No photos yet.\nCapture your first photo!",
                    fontSize = 17.sp,
                    color = Color.Gray
                )
            }

        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = photos,
                    key = { it.id }
                ) { photo ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Column {

                            AndroidView(
                                factory = { context ->
                                    ImageView(context).apply {
                                        scaleType =
                                            ImageView.ScaleType.CENTER_CROP
                                    }
                                },
                                update = { imageView ->
                                    val file = File(photo.imagePath)

                                    if (file.exists()) {
                                        imageView.setImageURI(
                                            Uri.fromFile(file)
                                        )
                                    } else {
                                        imageView.setImageDrawable(null)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                            )

                            Text(
                                text = photo.note.ifBlank {
                                    "No note added"
                                },
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 8.dp
                                ),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}