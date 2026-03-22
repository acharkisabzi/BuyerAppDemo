package com.example.buyerappdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buyerappdemo.screens.LoginScreen
import com.example.buyerappdemo.screens.ProductFeedScreen
import com.example.buyerappdemo.ui.theme.BuyerAppDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuyerAppDemoTheme {
                AppScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun AppScreen(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            LoginScreen(navController = navController)
        }
        composable(route = "productFeed") {
            ProductFeedScreen(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BuyerAppDemoTheme {
        AppScreen(modifier = Modifier.fillMaxSize())
    }
}