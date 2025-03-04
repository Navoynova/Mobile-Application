import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.example.newsapp40.R

// Data model for User
data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val password: String = ""
)

fun updateUser(userId: String, updatedUser: User) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users").document(userId)  // The document ID of the user
        .set(updatedUser)
        .addOnSuccessListener {
            // Handle success
            println("User updated successfully")
        }
        .addOnFailureListener { exception ->
            // Handle failure
            println("Error updating user: ${exception.message}")
        }
}

@Composable
fun UserManagementScreen(navController: NavController) {
    var users by remember { mutableStateOf<List<User>>(emptyList()) }

    val db = FirebaseFirestore.getInstance()

    // Fetch users from Firestore
    LaunchedEffect(Unit) {
        db.collection("users")  // Replace "users" with your Firestore collection name
            .get()
            .addOnSuccessListener { result: QuerySnapshot ->
                users = result.documents.map { document ->
                    val userId = document.id  // Get the document ID as userId
                    val name = document.getString("name") ?: ""
                    val email = document.getString("email") ?: ""
                    val role = document.getString("role") ?: ""
                    val password = document.getString("password") ?: ""
                    User(userId, name, email, role, password)
                }
            }
    }

    Image(
        painter = painterResource(id = R.drawable.back_arrow),
        contentDescription = "Back Button",
        modifier = Modifier
            .size(40.dp)
            .offset(16.dp,16.dp)
            .clickable {
                navController.navigate("admin_dashboard") {
                    popUpTo("admin_dashboard") { inclusive = true }
                }
            }
    )

    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "User Management",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("add_new_user") },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF165A71)),
            modifier = Modifier.width(320.dp).height(60.dp)
        ) {
            Text("Add New User", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Display users in a list of cards
        LazyColumn {
            items(users) { user ->
                UserCard(user)
            }
        }
    }

}



@Composable
fun UserCard(user: User) {
    var expanded by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var password by remember { mutableStateOf(user.password) }

    val db = FirebaseFirestore.getInstance()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(0.dp, 0.dp, 0.dp, 16.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = email, fontSize = 14.sp)
            Text(text = "Role: ${user.role}", fontSize = 14.sp)

            if (expanded) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { editMode = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4bae42)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Edit", color = Color.White)
                    }

                    Button(
                        onClick = {
                            db.collection("users").document(user.userId)
                                .delete()
                                .addOnSuccessListener {
                                    // Handle success (optional)
                                    println("User deleted successfully")
                                }
                                .addOnFailureListener { exception ->
                                    // Handle failure (optional)
                                    println("Error deleting user: ${exception.message}")
                                }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFe53434)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Delete", color = Color.White)
                    }
                }
            }

            if (editMode) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        // Create an updated user object
                        val updatedUser = User(userId = user.userId, name = name, email = email, role = user.role, password = password)

                        // Call the updateUser function
                        updateUser(user.userId, updatedUser)  // Update based on userId
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save", color = Color.White)
                }
            }
        }
    }
}
