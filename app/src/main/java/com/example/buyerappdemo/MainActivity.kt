package com.example.buyerappdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.buyerappdemo.models.ProductModel
import com.example.buyerappdemo.screens.LoginScreen
import com.example.buyerappdemo.screens.ProductFeedScreen
import com.example.buyerappdemo.screens.ViewProductScreen
import com.example.buyerappdemo.supabase.supabase
import com.example.buyerappdemo.ui.theme.ADAtSecondary
import com.example.buyerappdemo.ui.theme.BuyerAppDemoTheme
import com.example.buyerappdemo.viewmodels.AuthViewModel
import io.github.jan.supabase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize Supabase with Context
        com.example.buyerappdemo.supabase.Supabase.init(applicationContext)

        enableEdgeToEdge()
        setContent {
            BuyerAppDemoTheme {
                AppScreen(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun AppScreen(modifier: Modifier) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authUiState by authViewModel.uiState.collectAsState()

    // Wait until we know the auth status before showing the UI
    if (authUiState.isAuthenticated == null) {
        // Show a simple loading screen so Login doesn't "flash"
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = ADAtSecondary)
        }
    } else {
        NavHost(
            navController = navController,
            // Set startDestination based on login status
            startDestination = if (authUiState.isAuthenticated == true) "productFeed" else "login",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350, easing = EaseOutCubic)
                ) + fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350, easing = EaseInCubic)
                ) + fadeOut(animationSpec = tween(350))
            }
        ) {
            composable(route = "login") { LoginScreen(navController = navController) }
            composable(route = "productFeed") { ProductFeedScreen(navController = navController) }
            composable<ProductModel> { backStackEntry ->
                val product: ProductModel = backStackEntry.toRoute<ProductModel>()
                ViewProductScreen(navController = navController, productModel = product)
            }
        }
    }
}

@Preview
@Composable
fun GreetingPreview() {
    BuyerAppDemoTheme {
        AppScreen(Modifier.fillMaxSize())
    }
}