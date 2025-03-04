package com.example.newsapp40

import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

// Data class for article
data class Article(
    val title: String,
    val content: String,
    val category: String,
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis() // For ordering by date
)

@Composable
fun CreateNewArticleScreen(navController: NavController) {
    val context = LocalContext.current
    var titleText by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Business") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }


    val categories = listOf("Business", "Sports", "Economy", "Health")

    // Firebase Firestore instance
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()

    // Handle image picker result
    val getImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        // Back Button (using Image in JPEG format)
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = "Back Button",
            modifier = Modifier
                .size(40.dp)
                .clickable { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title: "Create New Article"
        Text(
            text = "Create New Article",
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card with form fields
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Title Input
                Text(text = "Title", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    placeholder = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Content Input
                Text(text = "Content", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = contentText,
                    onValueChange = { contentText = it },
                    placeholder = { Text("Content...") },
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Select Category
                Text(text = "Select Category", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { category ->
                        Button(
                            onClick = { selectedCategory = category },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedCategory == category) Color(0xFF165A71) else Color.LightGray
                            )
                        ) {
                            Text(text = category, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Add Image Button (Open image picker)
                Button(
                    onClick = { getImage.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Add Image")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button to upload data to Firestore
                Button(
                    onClick = {
                        // Create Article object with form data
                        val article = ReviewArticles(
                            title = titleText,
                            content = contentText,
                            category = selectedCategory
                        )

                        // Check if image is selected
                        if (selectedImageUri != null) {
                            val storageRef: StorageReference =
                                storage.reference.child("article_images/${UUID.randomUUID()}.jpg")

                            // Upload the image to Firebase Storage
                            storageRef.putFile(selectedImageUri!!)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Get the download URL after successful upload
                                        storageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                                            // Create article with image URL
                                            val articleWithImage = article.copy(imageUrl = imageUrl.toString())

                                            // Upload the article with image URL to Firestore
                                            db.collection("articles")
                                                .add(articleWithImage)
                                                .addOnSuccessListener { documentReference ->
                                                    Log.d("CreateArticle", "DocumentSnapshot added with ID: ${documentReference.id}")
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.w("CreateArticle", "Error adding document", e)
                                                }
                                        }
                                    } else {
                                        // Handle image upload failure
                                        Log.w("CreateArticle", "Error uploading image", task.exception)
                                    }
                                }
                        } else {
                            // Upload article without image
                            db.collection("articles")
                                .add(article)
                                .addOnSuccessListener { documentReference ->
                                    Log.d("CreateArticle", "DocumentSnapshot added with ID: ${documentReference.id}")
                                }
                                .addOnFailureListener { e ->
                                    Log.w("CreateArticle", "Error adding document", e)
                                }
                        }

                        // Clear the fields or reset the form after submission
                        titleText = ""
                        contentText = ""
                        selectedCategory = "Business" // Reset category to default
                        selectedImageUri = null // Reset image selection
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End)
                ) {
                    Text(text = "Submit", color = Color.White)
                }

            }
        }
    }
}
