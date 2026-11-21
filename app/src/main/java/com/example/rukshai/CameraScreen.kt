package com.example.rukshai

import android.Manifest
import android.content.pm.PackageManager
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File

@Composable
fun CameraScreen(
    onBack: () -> Unit,
    onPhotoCaptured: (String) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var statusMessage by remember {
        mutableStateOf("Position your camera")
    }

    var isCapturing by remember {
        mutableStateOf(false)
    }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            hasPermission = granted

            if (!granted) {
                statusMessage = "Camera permission was denied."
            }
        }

    LaunchedEffect(hasPermission, lifecycleOwner) {

        if (hasPermission) {

            val cameraProviderFuture =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({

                try {
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }

                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture
                    )

                } catch (exception: Exception) {
                    statusMessage =
                        "Unable to start camera: ${exception.message}"
                }

            }, ContextCompat.getMainExecutor(context))
        }
    }

    fun capturePhoto() {

        if (isCapturing) return

        isCapturing = true
        statusMessage = "Capturing photo..."

        val picturesDirectory =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ?: context.filesDir

        if (!picturesDirectory.exists()) {
            picturesDirectory.mkdirs()
        }

        val photoFile = File(
            picturesDirectory,
            "QuickCam_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(
                    outputFileResults: ImageCapture.OutputFileResults
                ) {
                    isCapturing = false
                    onPhotoCaptured(photoFile.absolutePath)
                }

                override fun onError(exception: ImageCaptureException) {
                    isCapturing = false
                    statusMessage =
                        "Capture failed: ${exception.message}"
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10131A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(onClick = onBack) {
                Text(
                    text = "← Back",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "QuickCam",
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.width(55.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (hasPermission) {

            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = statusMessage,
                color = Color.White,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { capturePhoto() },
                enabled = !isCapturing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3478F6)
                )
            ) {
                Text(
                    text = if (isCapturing) "Capturing..." else "Capture Photo",
                    fontSize = 18.sp
                )
            }

        } else {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Camera permission required",
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            ) {
                Text("Allow Camera Access")
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Environment
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.compose.LocalLifecycleOwner
//import java.io.File
//
//@Composable
//fun CameraScreen(onBack: () -> Unit) {
//
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    var hasPermission by remember {
//        mutableStateOf(
//            ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED
//        )
//    }
//
//    var statusMessage by remember {
//        mutableStateOf("Position your camera")
//    }
//
//    val imageCapture = remember {
//        ImageCapture.Builder()
//            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//            .build()
//    }
//
//    val previewView = remember {
//        PreviewView(context).apply {
//            scaleType = PreviewView.ScaleType.FILL_CENTER
//            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
//        }
//    }
//
//    val permissionLauncher =
//        rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.RequestPermission()
//        ) { granted ->
//            hasPermission = granted
//            if (!granted) {
//                statusMessage = "Camera permission was denied."
//            }
//        }
//
//    LaunchedEffect(hasPermission, lifecycleOwner) {
//
//        if (hasPermission) {
//
//            val cameraProviderFuture =
//                ProcessCameraProvider.getInstance(context)
//
//            cameraProviderFuture.addListener({
//
//                try {
//                    val cameraProvider = cameraProviderFuture.get()
//
//                    val preview = Preview.Builder().build().also {
//                        it.surfaceProvider = previewView.surfaceProvider
//                    }
//
//                    val cameraSelector =
//                        CameraSelector.DEFAULT_BACK_CAMERA
//
//                    cameraProvider.unbindAll()
//
//                    cameraProvider.bindToLifecycle(
//                        lifecycleOwner,
//                        cameraSelector,
//                        preview,
//                        imageCapture
//                    )
//
//                } catch (exception: Exception) {
//                    statusMessage =
//                        "Unable to start camera: ${exception.message}"
//                }
//
//            }, ContextCompat.getMainExecutor(context))
//        }
//    }
//
//    fun capturePhoto() {
//
//        val picturesDirectory =
//            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                ?: context.filesDir
//
//        val photoFile = File(
//            picturesDirectory,
//            "QuickCam_${System.currentTimeMillis()}.jpg"
//        )
//
//        val outputOptions =
//            ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(context),
//            object : ImageCapture.OnImageSavedCallback {
//
//                override fun onImageSaved(
//                    outputFileResults: ImageCapture.OutputFileResults
//                ) {
//                    statusMessage = "Photo saved successfully!"
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    statusMessage =
//                        "Capture failed: ${exception.message}"
//                }
//            }
//        )
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF10131A))
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            TextButton(onClick = onBack) {
//                Text(
//                    text = "← Back",
//                    color = Color.White,
//                    fontSize = 16.sp
//                )
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Text(
//                text = "QuickCam",
//                color = Color.White,
//                fontSize = 20.sp
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Spacer(modifier = Modifier.width(55.dp))
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        if (hasPermission) {
//
//            AndroidView(
//                factory = { previewView },
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Text(
//                text = statusMessage,
//                color = Color.White,
//                fontSize = 14.sp
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Button(
//                onClick = { capturePhoto() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(58.dp),
//                shape = RoundedCornerShape(18.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF3478F6)
//                )
//            ) {
//                Text(
//                    text = "Capture Photo",
//                    fontSize = 18.sp
//                )
//            }
//
//        } else {
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Text(
//                text = "Camera permission required",
//                color = Color.White,
//                fontSize = 20.sp
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Button(
//                onClick = {
//                    permissionLauncher.launch(Manifest.permission.CAMERA)
//                }
//            ) {
//                Text("Allow Camera Access")
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//        }
//    }
//}