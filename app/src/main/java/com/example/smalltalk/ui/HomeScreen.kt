package com.example.smalltalk.ui.theme

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smalltalk.R
import com.example.smalltalk.data.Quote
import com.example.smalltalk.utils.Date
import com.example.smalltalk.viewModels.QuoteViewModel

@Composable
fun HomeScreen(quoteViewModel: QuoteViewModel) {
    val quote: String =  quoteViewModel.uiState.collectAsState().value

    Column (
        modifier = Modifier.
        fillMaxSize().
        background(color = Color(0xFF10161B)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(60.dp))

        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                "QuoteIt",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Image(painter = painterResource(R.drawable.app_icon), contentDescription = "top bar logo")

        }
        Spacer(modifier = Modifier.height(50.dp))
        Column (
            modifier = Modifier.padding(30.dp)
                .fillMaxSize(),
        ){
            Text(
                text = quote,
                fontSize = 30.sp,
                color = Color.White,
                textAlign = TextAlign.Justify,
                fontFamily = FontFamily.Serif,
                lineHeight = 35.sp
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text("PRAWIN", color = Color.White, fontSize = 25.sp)
            Text("fact", color=Color(0xFFDDDDDD), fontSize = 20.sp)
        }
    }
}
