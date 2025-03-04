package com.example.newsapp40

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Delay for 2 seconds and navigate to User Selection Screen
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("user_selection") {
            popUpTo("splash") { inclusive = true } // Removes splash from back stack
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_screen), // Your splash image
            contentDescription = "Splash Screen",
            modifier = Modifier.fillMaxSize()
        )
    }
}