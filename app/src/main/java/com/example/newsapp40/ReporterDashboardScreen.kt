package com.example.newsapp40

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun ReportersDashboard(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFEFEFEF)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.news_nexus_logo_1),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clickable { navController.navigate("profile") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Reporter's Dashboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(90.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ReporterDashboardCard(title = "Pending Articles", iconRes = R.drawable.pending_articles,navController = navController)
                    Spacer(modifier = Modifier.height(24.dp))
                ReporterDashboardCard(title = "Approved Articles", iconRes = R.drawable.approved_articles,navController = navController)
                    Spacer(modifier = Modifier.height(24.dp))
                ReporterDashboardCard(title = "Rejected Articles", iconRes = R.drawable.rejected_articles,navController = navController)
                    Spacer(modifier = Modifier.height(24.dp))
                ReporterDashboardCard(title = "Create New Article", iconRes = R.drawable.plus,navController = navController)
        }
    }
}

@Composable
fun ReporterDashboardCard(title: String, iconRes: Int, navController: NavController) {
    Column(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
            .fillMaxWidth(0.4f)
            .clickable { if (title == "Create New Article") {
                // Navigate to the "Create New Article" screen
                navController.navigate("create_new_article")
            }
                // You can add other conditions here if you want to handle clicks differently for other cards
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}
