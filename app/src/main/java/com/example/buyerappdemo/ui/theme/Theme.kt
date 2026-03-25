package com.example.buyerappdemo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(

)

private val LightColorScheme = lightColorScheme(
    primary = DSecondary,
    onPrimary = DOnSecondary,
    secondary = DSecondary,
    onSecondary = DOnSecondary,
    surface = DSurface,
    onSurface = DOnSurface,          // this controls TextField text color
    onSurfaceVariant = DOnSurfaceVariant,  // this controls label/hint color
    outline = DOutline,              // this controls border color
    error = DError,
)

@Composable
fun BuyerAppDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

// In Theme.kt
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography.copy(
            bodyLarge = Typography.bodyLarge.copy(color = Color(0xFF2D3335))
        ),
        content = content
    )
}