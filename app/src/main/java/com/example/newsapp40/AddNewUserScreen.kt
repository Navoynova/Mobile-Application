package com.example.newsapp40

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.OutlinedTextField
import androidx.navigation.NavController

@Composable
fun AddNewUserScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("") }

    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Back Button
        Image(
            painter = painterResource(id = R.drawable.back_arrow), // Ensure you have a back arrow image in drawable
            contentDescription = "Back Button",
            modifier = Modifier
                .size(40.dp)
                .clickable { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Add New User",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card with fields
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Name Field
                Text(text = "Name", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Name...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email Field
                Text(text = "Email", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                Text(text = "Password", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Role Selection
                Text(text = "Role", fontSize = 16.sp, fontWeight = FontWeight.Bold,)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RoleButton(role = "Admin", selectedRole = selectedRole, onClick = { selectedRole = "Admin" })
                    RoleButton(role = "Editor", selectedRole = selectedRole, onClick = { selectedRole = "Editor" })
                    RoleButton(role = "Reporter", selectedRole = selectedRole, onClick = { selectedRole = "Reporter" })
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Add Button
                Button(
                    onClick = {
                        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && selectedRole.isNotEmpty()) {
                            val user = User(
                                name = name,
                                email = email,
                                password = password, // Ensure to hash the password before storing
                                role = selectedRole
                            )
                            addUserToFirestore(user, auth, firestore, navController)
                        } else {
                            Toast.makeText(navController.context, "All fields must be filled", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4bae42)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add", color = Color.White)
                }
            }
        }
    }
}

// Function to add user to Firestore
fun addUserToFirestore(user: User, auth: FirebaseAuth, firestore: FirebaseFirestore, navController: NavController) {
    // Create Firebase Auth user first
    auth.createUserWithEmailAndPassword(user.email, user.password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = auth.currentUser
                currentUser?.let {
                    val userRef = firestore.collection("users").document(it.uid)
                    userRef.set(user)
                        .addOnSuccessListener {
                            // Success
                            Toast.makeText(navController.context, "User added successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            // Failure
                            Toast.makeText(navController.context, "Error adding user: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(navController.context, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}

// Role button to select the user role
@Composable
fun RoleButton(role: String, selectedRole: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (role == selectedRole) Color(0xFF165A71) else Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = role, color = if (role == selectedRole) Color.White else Color.Black)
    }
}

// User data class for Firestore storage
data class User(
    val name: String,
    val email: String,
    val password: String, // Ensure to hash passwords for security
    val role: String
)
