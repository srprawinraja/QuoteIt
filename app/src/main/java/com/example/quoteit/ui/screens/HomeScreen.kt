package com.example.quoteit.ui.theme

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quoteit.R
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.data.Quote
import com.example.quoteit.viewModels.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    val uiData =  homeViewModel.uiState.collectAsState().value
    val uiTagData by homeViewModel.tagsFlow.collectAsState()

    Column (
        modifier = Modifier.
        fillMaxSize().
        background(color = themeColors().background)
            .padding(30.dp).systemBarsPadding(),
    ){

        Row (
            modifier = Modifier.fillMaxWidth().wrapContentSize(),
            horizontalArrangement = Arrangement.Center,
        ){
            Image(painter = painterResource(R.drawable.quote_left_side_icon), contentDescription = "top bar logo", modifier = Modifier.offset(x = -5.dp, y = -20.dp))

            Text(
                "QuoteIt",
                color = themeColors().text,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Image(painter = painterResource(R.drawable.quote_right_side_icon), contentDescription = "top bar logo", modifier = Modifier.offset(x = 5.dp, y = -20.dp))

        }
            Spacer(modifier = Modifier.height(80.dp))
            val selectedId = remember { mutableStateOf(0) }
            Row (
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ){

                Button(
                    onClick = {
                        selectedId.value = 0
                        homeViewModel.updateTodayQuote()
                    },
                    colors = ButtonColors(
                        containerColor = themeColors().background,
                        contentColor = themeColors().text,
                        disabledContentColor = themeColors().text,
                        disabledContainerColor = themeColors().background
                    ),
                    modifier = Modifier.then(
                        if (selectedId.value == 0) {
                            Modifier.border(
                                border = BorderStroke(1.dp, themeColors().border),
                                shape = CircleShape
                            )
                        } else  Modifier.border(
                            border = BorderStroke(1.dp, themeColors().lightBorderColor),
                            shape = CircleShape
                        )
                    )
                ) {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        text = "Today",
                        color = themeColors().text,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                uiTagData.map { tag->
                    val mark = remember{ mutableStateOf(true)}
                    if(mark.value){
                        Button(
                            onClick = {
                                selectedId.value = tag.id
                                homeViewModel.updateSelectedTagQuote(tag.tagId)
                            },
                            colors = ButtonColors(
                                containerColor = themeColors().background,
                                contentColor = themeColors().text,
                                disabledContentColor = themeColors().text,
                                disabledContainerColor = themeColors().background
                            ),
                            modifier = Modifier.then(
                                if (selectedId.value == tag.id) {
                                    Modifier.border(
                                        border = BorderStroke(1.dp, themeColors().border),
                                        shape = CircleShape
                                    )// Color(0xFF2C2F36)
                                } else  Modifier.border(
                                    border = BorderStroke(1.dp, themeColors().lightBorderColor),
                                    shape = CircleShape
                                )
                            )
                        ) {
                            Text(
                                modifier = Modifier.wrapContentSize(),
                                text = tag.tagName,
                                color = themeColors().text,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            if (selectedId.value == tag.id) {
                                Icon(
                                    modifier = Modifier.clickable(onClick = {
                                        homeViewModel.deleteTag(tag)
                                    }),
                                    painter = painterResource(R.drawable.cancel_icon),
                                    contentDescription = "cancel icon",
                                    tint = themeColors().text
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
                Icon(
                    painter = painterResource(R.drawable.add_icon),
                    contentDescription = "add",
                    tint = themeColors().text,
                    modifier = Modifier.size(45.dp).clickable(onClick = {
                        navController.navigate("Tags")
                    })
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            when (val result = uiData) {
                is NetworkResponse.Success -> {
                    ShowQuote(
                        navController,
                        homeViewModel,
                        uiData,
                        result.data.quote,
                        result.data.author,
                        result.data.tag
                    )
                }

                is NetworkResponse.ErrorQuote -> {
                    ShowQuote(
                        navController,
                        homeViewModel,
                        null,
                        result.data.quote,
                        result.data.author,
                        result.data.tag
                    )
                }

                is NetworkResponse.LoadingQuote -> {
                    ShowQuote(
                        navController,
                        homeViewModel,
                        null,
                        result.data.quote,
                        result.data.author,
                        result.data.tag
                    )
                }

                else -> {

                }
            }


    }
}

@Composable
fun ShowQuote(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    uiData: NetworkResponse.Success<Quote>?,
    quote: String,
    author: String,
    tag: String
){
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = quote,
            fontSize = 30.sp,
            color = themeColors().text,
            fontFamily = FontFamily.Serif,
            lineHeight = 35.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(modifier= Modifier.wrapContentSize(), text=author.uppercase(), color = themeColors().text, fontSize = 20.sp)
        Text(modifier= Modifier.wrapContentSize(), text=tag, color=themeColors().lightText, fontSize = 15.sp)
    }
    Spacer(modifier = Modifier.height(20.dp))
    MiddleRowButtons(navController, uiData, homeViewModel, quote)
}

@Composable
fun MiddleRowButtons(
    navController: NavHostController,
    uiData: NetworkResponse.Success<Quote>?,
    homeViewModel: HomeViewModel,
    quote: String
){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ){
        IconButton(
            onClick = {
                    navController.navigate("Saved")
            },
            modifier = Modifier
                .size(40.dp)
                .background(color = themeColors().surface, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.book_icon),
                contentDescription = "save",
                tint = themeColors().text,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.width(30.dp))
        IconButton(
            onClick = {
                uiData?.let {
                    homeViewModel.saveQuote(uiData.data.id, uiData.data.quote, uiData.data.author, uiData.data.tag)
                }
            },
            modifier = Modifier
                .size(40.dp)
                .background(color = themeColors().surface, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.bookmark_icon),
                contentDescription = "save",
                tint = themeColors().text,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.width(30.dp))
        IconButton(
            onClick = { navController.navigate("Share/$quote") },
            modifier = Modifier
                .size(40.dp)
                .background(color = themeColors().surface, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.share_icon),
                contentDescription = "Share",
                tint = themeColors().text,
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
