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
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items


@Composable
fun ReviewArticlesScreen(navController: NavController) {
    var articles by remember { mutableStateOf<List<ReviewArticles>>(emptyList()) }
    val db = FirebaseFirestore.getInstance()

    // Fetching articles from Firestore
    LaunchedEffect(Unit) {
        db.collection("articles")
            .get()
            .addOnSuccessListener { result ->
                articles = result.toObjects(ReviewArticles::class.java)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Back Button
        Image(
            painter = painterResource(id = R.drawable.back_arrow), // Place your JPEG back button in res/drawable
            contentDescription = "Back",
            modifier = Modifier
                .size(32.dp)
                .clickable { navController.popBackStack() }
        )

        // Title
        Text(
            text = "Review Articles",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Displaying articles
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(articles) { article ->
                ReviewNewsCard(
                    imageUrl = article.imageUrl ?: "https://example.com/default_image.jpg", // Fallback if imageUrl is null
                    title = article.title,
                    category = article.category,
                    timestamp = article.timestamp,
                    onClick = {
                        // Navigate to the article detail screen
                        navController.navigate("article_details_screen/${article.title}")
                    }
                )
            }
        }
    }
}

@Composable
fun ReviewNewsCard(imageUrl: String, title: String, category: String, timestamp: Long, onClick: () -> Unit) {
    val formattedDate = remember(timestamp) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = Date(timestamp)
        dateFormat.format(date)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Article Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).padding(8.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = category, fontSize = 12.sp, color = Color.Gray)
                Text(text = formattedDate, fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}

// Data class for articles
data class ReviewArticles(
    val id: String = "",
    val imageUrl: String? = null,
    val title: String = "",
    val category: String = "",
    val content: String = "",
    val timestamp: Long = 0L
)
