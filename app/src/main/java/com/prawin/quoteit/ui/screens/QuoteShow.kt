package com.prawin.quoteit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prawin.quoteit.ui.theme.themeColors
import com.prawin.quoteit.viewModels.QuoteShowViewModel
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController

@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
@Composable
fun QuoteShow(quote: String, quoteShowViewModel: QuoteShowViewModel) {
    val captureController = rememberCaptureController()
    LaunchedEffect(captureController) {
        withFrameNanos {
            quoteShowViewModel.convertAndShareAsImage(captureController)
        }
    }

    Box(
        modifier = Modifier.wrapContentSize().capturable(captureController)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = themeColors().imgBackground)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = quote.uppercase(),
                modifier = Modifier.width(150.dp).wrapContentSize(),
                fontSize = 15.sp,
                color = themeColors().text,
                fontFamily = FontFamily.SansSerif,
                lineHeight = 25.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}