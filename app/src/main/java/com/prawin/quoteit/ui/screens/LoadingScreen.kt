package com.prawin.quoteit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.prawin.quoteit.ui.theme.themeColors

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(color = themeColors().background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}