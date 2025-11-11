package com.example.quoteit.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.quoteit.R
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.data.Quote
import com.example.quoteit.viewModels.QuoteViewModel

@Composable
fun HomeScreen(navController: NavHostController, quoteViewModel: QuoteViewModel) {
    val uiData: NetworkResponse<Quote> =  quoteViewModel.uiState.collectAsState().value

    Column (
        modifier = Modifier.
        fillMaxSize().
        background(color = Color(0xFF10161B))
            .padding(30.dp),
    ){
        Spacer(modifier = Modifier.height(20.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                "QuoteIt",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Image(painter = painterResource(R.drawable.app_icon), contentDescription = "top bar logo")

        }
        Spacer(modifier = Modifier.height(200.dp))

        when (val result = uiData) {
            is NetworkResponse.Success -> {
                ShowQuote(navController, result.data.content, result.data.author, result.data.tags[0])
            }

            is NetworkResponse.ErrorQuote  -> {
                ShowQuote(navController, result.data.content, result.data.author, result.data.tags[0])
            }

            is NetworkResponse.LoadingQuote -> {
                ShowQuote(navController, result.data.content, result.data.author, result.data.tags[0])
            }

            else -> {

            }
        }
        Spacer(modifier = Modifier.height(100.dp))
        Row (
            modifier = Modifier.fillMaxWidth().wrapContentSize(),
        ){


        }
    }
}

@Composable
fun ShowQuote(navController: NavHostController, quote: String, author: String, tag: String){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = quote,
            fontSize = 30.sp,
            color = Color.White,
            fontFamily = FontFamily.Serif,
            lineHeight = 35.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(modifier= Modifier.wrapContentSize(), text=author.uppercase(), color = Color.White, fontSize = 20.sp)
        Text(modifier= Modifier.wrapContentSize(), text=tag, color=Color(0xFFDDDDDD), fontSize = 15.sp)
    }
    Spacer(modifier = Modifier.height(20.dp))
    MiddleRowButtons(navController, quote)

}

@Composable
fun MiddleRowButtons(navController: NavHostController, quote: String){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ){
        Box(
            modifier = Modifier
                .size(40.dp) // diameter
                .background(color = Color(0xFF293540), shape = CircleShape)
                .clickable{
                    navController.navigate("Share/$quote")
                },
            contentAlignment = Alignment.Center
        ) {
            Image(painterResource(R.drawable.share_icon), contentDescription = "share button",
                modifier = Modifier.width(30.dp).height(30.dp)
                    .padding(end =5.dp))
        }
    }
}

/*
 when (val result = uiData) {
                is NetworkResponse.Success -> {
                    showQuote(result.data.content)
                }

                is NetworkResponse.Error -> {
                    Log.e("hello there",city)
                    Text(text = result.message)
                }

                NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }

                else -> {
                    // nothing to display
                }
            }
 */
