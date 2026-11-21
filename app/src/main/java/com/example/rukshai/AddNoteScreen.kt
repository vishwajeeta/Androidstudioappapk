package com.example.rukshai

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

@Composable
fun AddNoteScreen(
    imagePath: String,
    photoViewModel: PhotoViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {

    var note by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val bitmap = remember(imagePath) {
        BitmapFactory.decodeFile(imagePath)
    }

    fun savePhoto(noteText: String) {
        if (isSaving) return

        isSaving = true
        errorMessage = null

        photoViewModel.savePhoto(
            imagePath = imagePath,
            note = noteText
        ) { success ->

            if (success) {
                onSaved()
            } else {
                isSaving = false
                errorMessage = "Unable to save photo. Please try again."
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FC))
            .padding(20.dp)
    ) {

        TextButton(
            onClick = onCancel,
            enabled = !isSaving
        ) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Add a Note",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF172B4D)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add details to help you remember this photo.",
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Captured photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Unable to load photo preview")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving,
            placeholder = {
                Text("Write something about this photo...")
            },
            label = {
                Text("Note (optional)")
            },
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(14.dp)
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { savePhoto(note.trim()) },
            enabled = !isSaving && File(imagePath).exists(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3478F6)
            )
        ) {
            Text(
                text = if (isSaving) "Saving..." else "Save Photo",
                fontSize = 17.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { savePhoto("") },
            enabled = !isSaving && File(imagePath).exists(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Skip Note")
        }
    }
}