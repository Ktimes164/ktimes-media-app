package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.MediaViewModel
import com.example.ui.screens.AdminGarageScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DetailScreen
import com.example.ui.theme.KtimesMediaTheme
import com.example.ui.theme.StudioDarkBg

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KtimesMediaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = StudioDarkBg
                ) {
                    KtimesMediaApp()
                }
            }
        }
    }
}

@Composable
fun KtimesMediaApp(
    viewModel: MediaViewModel = viewModel()
) {
    val navController = rememberNavController()
    val selectedDetailItem by viewModel.selectedItemForDetail.collectAsStateWithLifecycle()

    if (selectedDetailItem != null) {
        DetailScreen(
            item = selectedDetailItem!!,
            viewModel = viewModel,
            onBack = { viewModel.closeItemDetail() }
        )
    } else {
        NavHost(
            navController = navController,
            startDestination = "dashboard"
        ) {
            composable("dashboard") {
                DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToAdmin = { navController.navigate("admin") },
                    onItemClick = { item -> viewModel.openItemDetail(item) }
                )
            }

            composable("admin") {
                AdminGarageScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
