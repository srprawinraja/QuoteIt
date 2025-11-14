package com.example.quoteit.ui.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


data class AppThemeData(
    val background: Color,
    val surface: Color,
    val text: Color,
    val border: Color,
    val lightText: Color,
    val imgBackground: Color,
)
val DarkTheme = AppThemeData(
    background = DarkBlue100,
    surface = Blue100,
    text = White,
    border = Cyan,
    lightText = LightWhite,
    imgBackground = Black100
)

val LightTheme = AppThemeData(
    background = White,
    surface = LightGrey,
    text = Black100,
    border = Cyan,
    lightText = LightDark,
    imgBackground = White
)
val LocalAppTheme = staticCompositionLocalOf { LightTheme }

//background = DarkBlue100,
//surface = Blue100,
//secondary = Black100,
//onSecondary = White,

@Composable
fun QuoteItTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val theme = if (darkTheme) DarkTheme else LightTheme

    CompositionLocalProvider(LocalAppTheme provides theme) { content() }
}
@Composable
fun themeColors() = LocalAppTheme.current
