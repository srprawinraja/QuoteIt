package com.example.smalltalk.ui.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smalltalk.data.Quote
import com.example.smalltalk.utils.Date
import com.example.smalltalk.utils.SharedPreferenceUtility
import com.example.smalltalk.viewModels.QuoteViewModel

@Composable
fun HomeScreen(quoteViewModel: QuoteViewModel, activity: Activity) {
    lateinit var quote: String
    val dateStr = Date.getDate()
    if(SharedPreferenceUtility.isContains(activity, dateStr)){
        quote = SharedPreferenceUtility.get(activity,dateStr);
    } else {
        quote = quoteViewModel.uiState.collectAsState().value
        SharedPreferenceUtility.save(activity, dateStr, quote);
    }

    Column (
        modifier = Modifier.
        fillMaxSize().
        background(color = Color(0xFF10161B)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("small talk",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold)
        Column (
            modifier = Modifier.padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = quote,
                fontSize = 30.sp,
                color = Color.White,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text("PRAWIN", color = Color.White, fontSize = 18.sp)
            Text("fact", color=Color.White)
        }


    }
}
