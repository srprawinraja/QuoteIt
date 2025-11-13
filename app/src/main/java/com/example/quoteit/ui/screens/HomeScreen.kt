package com.example.quoteit.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quoteit.R
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.data.Quote
import com.example.quoteit.db.tag.TagEntity
import com.example.quoteit.ui.screens.ShowListOfTags
import com.example.quoteit.viewModels.HomeViewModel

@Composable
fun HomeScreen(navController: NavHostController, homeViewModel: HomeViewModel) {
    val uiData: NetworkResponse<Quote> =  homeViewModel.uiState.collectAsState().value
    val uiTagData by homeViewModel.tagsFlow.collectAsState()

    val lists = mutableListOf<String>(
        "Inspiration",
        "Motivational",
        "god",
        "angry"
    )
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
        Spacer(modifier = Modifier.height(150.dp))

                LazyRow(
                    modifier = Modifier
                        .wrapContentSize(),
                ) {
                    items(uiTagData.size) { index ->
                        val marked: MutableState<Boolean> =
                            remember { mutableStateOf(uiTagData.get(index).tagMarked) }
                        if (marked.value) {
                            if (!uiTagData.get(index).isImg) {
                                Button(
                                    onClick = {
                                        if (uiTagData.get(index).tagCached) {
                                            homeViewModel.changeTodayQuote()
                                        } else homeViewModel.changeSelectedQuote(uiTagData.get(index).tagSlug)
                                    },
                                    border = BorderStroke(1.dp, Color.Cyan),
                                    colors = ButtonColors(
                                        containerColor = Color(0xFF10161B),
                                        contentColor = Color.White,
                                        disabledContentColor = Color.White,
                                        disabledContainerColor = Color(0xFF10161B)
                                    )
                                ) {
                                    Text(
                                        modifier = Modifier.wrapContentSize(),
                                        text = uiTagData.get(index).tagName,
                                        color = Color(0xFFDDDDDD),
                                        fontSize = 15.sp
                                    )
                                }
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.add_icon),
                                    contentDescription = "add",
                                    tint = Color.White,
                                    modifier = Modifier.size(45.dp).clickable(onClick = {
                                        navController.navigate("Tags")
                                    })
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }



        Spacer(modifier = Modifier.height(20.dp))
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
        IconButton(
            onClick = { navController.navigate("Share/$quote") },
            modifier = Modifier
                .size(40.dp)
                .background(color = Color(0xFF293540), shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.share_icon),
                contentDescription = "Share",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
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
