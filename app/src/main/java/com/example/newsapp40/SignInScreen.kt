package com.example.newsapp40

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun SignInScreen(userType: String, onBackClick: () -> Unit, onSignInClick: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow), // Replace with your actual image resource name
            contentDescription = "Back",
            modifier = Modifier
                .size(32.dp)
                .clickable { onBackClick() }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Back Button Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Spacer(modifier = Modifier.width(8.dp))
                //Text(text = "Sign in as $userType", fontSize = 18.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logo Image
            Image(
                painter = painterResource(id = R.drawable.news_nexus_logo_2), // Replace with your actual logo resource name
                contentDescription = "App Logo",
                modifier = Modifier.size(250.dp) // Adjust the size as needed
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = "Enter Email") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.width(300.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = "Enter Password") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.width(300.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(90.dp))

            errorMessage?.let {
                Text(text = it, color = Color.Red, fontSize = 14.sp)
            }

            Button(
                onClick = { onSignInClick(email, password) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF165A71)),
                modifier = Modifier.width(180.dp).height(70.dp)
            ) {
                Text(text = "Sign In", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}


