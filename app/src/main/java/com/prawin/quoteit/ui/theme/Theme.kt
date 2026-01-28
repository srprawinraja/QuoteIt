package com.prawin.quoteit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


data class AppThemeData(
    val background: Color,
    val surface: Color,
    val text: Color,
    val border: Color,
    val lightText: Color,
    val imgBackground: Color,
    val lightBorderColor: Color
)
val DarkTheme = AppThemeData(
    background = DarkBlue100,
    surface = Blue100,
    text = White,
    border = Cyan,
    lightText = LightWhite,
    imgBackground = Black100,
    lightBorderColor = darkGrey
)

val LightTheme = AppThemeData(
    background = White,
    surface = LightGrey,
    text = Black100,
    border = Cyan,
    lightText = LightDark,
    imgBackground = White,
    lightBorderColor = LightGrey
)
val LocalAppTheme = staticCompositionLocalOf { LightTheme }


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
