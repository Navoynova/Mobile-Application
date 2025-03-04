package com.example.newsapp40

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter


data class NewsArticle(
    val title: String,
    val imageUrl: String,
    val category: String,
    val categoryIcon: Int
)

@Composable
fun PendingArticlesScreen(navController: NavController) {
    var expandedArticle by remember { mutableStateOf<String?>(null) }
    val articles = listOf(
        NewsArticle(
            "All passengers survive crash landing as plane flips at Toronto airport",
            "https://example.com/crash.jpg",
            "Crime & Law",
            android.R.drawable.ic_menu_compass
        ),
        NewsArticle(
            "Rodgers urges Celtic bravery as Kane misses Bayern training",
            "https://example.com/sports.jpg",
            "Sports",
            android.R.drawable.ic_menu_gallery
        )
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow), // Use your JPEG resource
                contentDescription = "Back Button",
                modifier = Modifier.size(24.dp).clickable {navController.popBackStack()}
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pending Articles", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        articles.forEach { article ->
            NewsCard(article, expandedArticle == article.title) {
                expandedArticle = if (expandedArticle == article.title) null else article.title
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun NewsCard(article: NewsArticle, isExpanded: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(article.imageUrl),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(article.title, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = article.categoryIcon),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(article.category, color = Color.Gray)
                    }
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { /* Approve action */ }, colors = ButtonDefaults.buttonColors(Color.Green)) {
                        Text("Approve")
                    }
                    Button(onClick = { /* Reject action */ }, colors = ButtonDefaults.buttonColors(Color.Red)) {
                        Text("Reject")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* Publish action */ },
                    colors = ButtonDefaults.buttonColors(Color.Blue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Publish")
                }
            }
        }
    }
}
