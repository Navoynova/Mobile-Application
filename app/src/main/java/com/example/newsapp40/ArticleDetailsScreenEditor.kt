package com.example.newsapp40

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ArticleDetailScreen(newsId: String, onBack: () -> Unit, navController: NavController) {
    var newsTitle by remember { mutableStateOf("") }
    var newsContent by remember { mutableStateOf("") }
    var newsImageUrl by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogIcon by remember { mutableStateOf(R.drawable.check_circle) }
    var dialogColor by remember { mutableStateOf(Color.Green) }

    val db = FirebaseFirestore.getInstance()

    // Fetch News from Firestore
    LaunchedEffect(newsId) {
        db.collection("news").document(newsId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    newsTitle = document.getString("title") ?: "No Title"
                    newsContent = document.getString("content") ?: "No Content"
                    newsImageUrl = document.getString("imageUrl") ?: ""
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching news", exception)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Back Button
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = "Back",
            modifier = Modifier
                .size(32.dp)
                .clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // News Title
        Text(
            text = newsTitle,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // News Image
        if (newsImageUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(newsImageUrl),
                contentDescription = "News Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // News Content
        Text(
            text = newsContent,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Approve & Reject Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Approve Button
            Button(
                onClick = {
                    dialogMessage = "Article Approved!"
                    dialogIcon = R.drawable.check_circle
                    dialogColor = Color.Green
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).padding(8.dp)
            ) {
                Text(text = "Approve", color = Color.White)
            }

            // Reject Button
            Button(
                onClick = {
                    dialogMessage = "Article Rejected!"
                    dialogIcon = R.drawable.cancel_circle // Add a red cancel icon in res/drawable
                    dialogColor = Color.Red
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).padding(8.dp)
            ) {
                Text(text = "Reject", color = Color.White)
            }
        }
    }

    // Show the approval/rejection dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        val status = if (dialogMessage.contains("Approved")) "approved" else "rejected"
                        db.collection("news").document(newsId)
                            .update("status", status)
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            title = {
                Icon(
                    painter = painterResource(id = dialogIcon),
                    contentDescription = dialogMessage,
                    tint = dialogColor,
                    modifier = Modifier.size(48.dp)
                )
            },
            text = {
                Text(
                    text = dialogMessage,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}


