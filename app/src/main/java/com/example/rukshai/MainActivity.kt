package com.example.rukshai



import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

private val PrimaryBlue = Color(0xFF3478F6)
private val AppBackground = Color(0xFFF5F7FC)
private val DarkText = Color(0xFF172B4D)

class MainActivity : ComponentActivity() {

    private val photoViewModel: PhotoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                QuickCamApp(photoViewModel)
            }
        }
    }
}

@Composable
fun QuickCamApp(photoViewModel: PhotoViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(
                onTakePhoto = {
                    navController.navigate("camera")
                },
                onViewPhotos = {
                    navController.navigate("saved")
                }
            )
        }

        composable("camera") {
            CameraScreen(
                onBack = {
                    navController.popBackStack()
                },
                onPhotoCaptured = { path ->
                    navController.navigate(
                        "addNote?path=${Uri.encode(path)}"
                    )
                }
            )
        }

        composable(
            route = "addNote?path={path}",
            arguments = listOf(
                navArgument("path") {
                    type = NavType.StringType
                }
            )
        ) { entry ->

            val path = entry.arguments?.getString("path") ?: ""

            AddNoteScreen(
                imagePath = path,
                photoViewModel = photoViewModel,
                onSaved = {
                    navController.popBackStack("home", false)
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable("saved") {
            SavedPhotosScreen(
                photoViewModel = photoViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    onTakePhoto: () -> Unit,
    onViewPhotos: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "QuickCamNotes",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Keep what matters.",
            fontSize = 17.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Your memories, organised.",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkText
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Capture a photo, add a note, and find it whenever you need it.",
            fontSize = 15.sp,
            color = Color.DarkGray,
            lineHeight = 23.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onTakePhoto,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue
            )
        ) {
            Text("Take a Photo", fontSize = 17.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onViewPhotos,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("View Saved Photos", fontSize = 16.sp)
        }
    }
}

@Composable
fun SimpleScreen(
    title: String,
    description: String,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description,
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onBack) {
            Text("Go Back")
        }
    }
}
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//
//private val PrimaryBlue = Color(0xFF3478F6)
//private val AppBackground = Color(0xFFF5F7FC)
//private val DarkText = Color(0xFF172B4D)
//
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            MaterialTheme {
//                QuickCamApp()
//            }
//        }
//    }
//}
//
//@Composable
//fun QuickCamApp() {
//
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = "home"
//    ) {
//
//        composable("home") {
//            HomeScreen(
//                onTakePhoto = {
//                    navController.navigate("camera")
//                },
//                onViewPhotos = {
//                    navController.navigate("saved")
//                }
//            )
//        }
//
//        composable("camera") {
//            CameraScreen(
//                onBack = {
//                    navController.popBackStack()
//                }
//            )
//        }
//
//        composable("saved") {
//            SavedPhotosScreen(
//                onBack = {
//                    navController.popBackStack()
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun HomeScreen(
//    onTakePhoto: () -> Unit,
//    onViewPhotos: () -> Unit
//) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(AppBackground)
//            .padding(24.dp)
//    ) {
//
//        Spacer(modifier = Modifier.height(40.dp))
//
//        Text(
//            text = "QuickCamNotes",
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Bold,
//            color = DarkText
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = "Keep what matters.",
//            fontSize = 17.sp,
//            color = Color.Gray
//        )
//
//        Spacer(modifier = Modifier.height(48.dp))
//
//        Text(
//            text = "Your memories, organised.",
//            fontSize = 22.sp,
//            fontWeight = FontWeight.SemiBold,
//            color = DarkText
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Text(
//            text = "Capture a photo, add a note, and find it whenever you need it.",
//            fontSize = 15.sp,
//            color = Color.DarkGray,
//            lineHeight = 23.sp
//        )
//
//        Spacer(modifier = Modifier.height(30.dp))
//
//        Button(
//            onClick = onTakePhoto,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp),
//            shape = RoundedCornerShape(14.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = PrimaryBlue
//            )
//        ) {
//            Text("Take a Photo", fontSize = 17.sp)
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        OutlinedButton(
//            onClick = onViewPhotos,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp),
//            shape = RoundedCornerShape(14.dp)
//        ) {
//            Text("View Saved Photos", fontSize = 16.sp)
//        }
//    }
//}
//
//@Composable
//fun SimpleScreen(
//    title: String,
//    description: String,
//    onBack: () -> Unit
//) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(AppBackground)
//            .padding(24.dp),
//        horizontalAlignment = Alignment.Start
//    ) {
//
//        Text(
//            text = title,
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Bold,
//            color = DarkText
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = description,
//            fontSize = 16.sp,
//            color = Color.DarkGray
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Button(onClick = onBack) {
//            Text("Go Back")
//        }
//    }
//}
