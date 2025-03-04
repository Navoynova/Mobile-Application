package com.example.newsapp40

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EditorsDashboard(navController: NavController) {
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
            text = "Editor's Dashboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                DashboardCard(title = "Review & Approve Articles", iconRes = R.drawable.review_icon, onClick = { navController.navigate("review_articles") })
                DashboardCard(title = "Approved Articles", iconRes = R.drawable.approved_articles)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                DashboardCard(title = "Rejected Articles", iconRes = R.drawable.rejected_articles)
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, iconRes: Int) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable { /* Handle click */ }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}
