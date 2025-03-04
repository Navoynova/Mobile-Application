package com.example.newsapp40


import UserManagementScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp40.ui.theme.NewsApp40Theme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.util.Log


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // Show the splash screen
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Initialize Firebase

        setContent {
            NewsApp40Theme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("user_selection") { UserSelectionScreen(navController) }
        composable("signIn/{userType}") { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: "default"
            SignInScreen(
                userType = userType,
                onBackClick = { navController.popBackStack() },
                onSignInClick = { email, password ->
                    handleSignIn(email, password, userType, navController)
                }
            )
        }
        composable("admin_dashboard") { AdminDashboard(navController) }

        composable("editor_dashboard") { EditorsDashboard(navController) }
        composable("reporter_dashboard") { ReportersDashboard(navController) }
        composable("create_new_article") { CreateNewArticleScreen(navController) }
        composable("userManagement") { UserManagementScreen(navController) }
        composable("add_new_user") { AddNewUserScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("pending_articles_admin") { PendingArticlesScreen(navController) }
        composable("review_articles") { ReviewArticlesScreen(navController) }
        composable("article_details_screen/{newsId}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
            ArticleDetailScreen(newsId, onBack = { navController.popBackStack() }, navController)
        }

    }
}


fun handleSignIn(email: String, password: String, userType: String, navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // Sign in the user with email and password
    auth.signInWithEmailAndPassword(email, password)
        .addOnSuccessListener { authResult ->
            val userId = authResult.user?.uid ?: return@addOnSuccessListener
            Log.d("handleSignIn", "Successfully signed in: $userId")

            // Retrieve user role from Firestore
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val role = document.getString("role")
                    Log.d("handleSignIn", "Retrieved role: $role")

                    if (role == null) {
                        showError("Role not found")
                        return@addOnSuccessListener
                    }

                    if (role != userType) {
                        showError("Role mismatch! Please log in with the correct user type.")
                    } else {
                        when (role) {
                            "Admin" -> {
                                Log.d("handleSignIn", "Navigating to Admin Dashboard")
                                navController.navigate("admin_dashboard") {
                                    popUpTo("signIn/{userType}") { inclusive = true }
                                }
                            }
                            "Editor" -> {
                                Log.d("handleSignIn", "Navigating to Editor Dashboard")
                                navController.navigate("editor_dashboard") {
                                    popUpTo("signIn/{userType}") { inclusive = true }
                                }
                            }
                            "Reporter" -> {
                                Log.d("handleSignIn", "Navigating to Reporter Dashboard")
                                navController.navigate("reporter_dashboard") {
                                    popUpTo("signIn/{userType}") { inclusive = true }
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    showError("Failed to retrieve user role")
                    Log.e("handleSignIn", "Error retrieving user role", it)
                }
        }
        .addOnFailureListener {
            showError("Invalid email or password")
            Log.e("handleSignIn", "Sign-in failed", it)
        }
}


fun showError(message: String) {
    // Display error message using Snackbar or Toast
}

