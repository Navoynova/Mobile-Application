package com.example.newsapp40

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UserSelectionScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.news_nexus_logo_2),
            contentDescription = "News Nexus Logo",
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Select User Type", fontSize = 20.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(34.dp))

        UserTypeButton("Admin", navController)
        UserTypeButton("Reporter", navController)
        UserTypeButton("Editor", navController)
    }
}

@Composable
fun UserTypeButton(userType: String, navController: NavController) {
    Button(
        onClick = { navController.navigate("signIn/$userType") },
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D5D75)),
        modifier = Modifier
            .width(300.dp)
            .height(70.dp)
            .padding(vertical = 11.dp)
    ) {
        Text(text = userType, fontSize = 16.sp, color = Color.White)
    }
}
