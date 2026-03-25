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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

    // 2. Track the Auth State
    // authenticated = true, notAuthenticated = false, null = still checking
    var isAuthenticated by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        // Check if there is a valid session saved on disk
        val session = supabase.auth.currentSessionOrNull()
        isAuthenticated = session != null
    }

    // 3. Wait until we know the auth status before showing the UI
    if (isAuthenticated == null) {
        // Show a simple loading screen so Login doesn't "flash"
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = ADAtSecondary)
        }
    } else {
        NavHost(
            navController = navController,
            // 4. Set startDestination based on login status
            startDestination = if (isAuthenticated == true) "productFeed" else "login",
            enterTransition = { fadeIn(animationSpec = tween(700)) },
            exitTransition = { fadeOut(animationSpec = tween(700)) }
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