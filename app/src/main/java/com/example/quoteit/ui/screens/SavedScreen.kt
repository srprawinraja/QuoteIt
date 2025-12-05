package com.example.quoteit.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quoteit.R
import com.example.quoteit.ui.theme.themeColors
import com.example.quoteit.viewModels.SavedViewModel

@Composable
fun SavedScreen(navController: NavHostController, savedScreenViewModel: SavedViewModel) {
    val uiSavedData by savedScreenViewModel.savedQuoteFlow.collectAsState()

    Column (
        modifier = Modifier.
        fillMaxSize().
        background(color = themeColors().background)
        ){
        Spacer(modifier = Modifier.fillMaxWidth().padding(30.dp))
        IconButton(
            onClick = {
                navController.navigate("SavedDetail")
            },
            modifier = Modifier.padding(start = 10.dp).wrapContentSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.left_icon),
                contentDescription = "left button",
                tint = themeColors().text
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text("Saved", color = themeColors().text, modifier = Modifier.padding(start = 30.dp), fontSize = 30.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Column (
            modifier = Modifier.fillMaxSize().horizontalScroll(rememberScrollState())
        ){

           // uiSavedData.forEach { it ->
                TagAndComposable(navController, "Inspiration")
                TagAndComposable(navController, "Motivation")
                TagAndComposable(navController, "Fear")

            // }
        }

    }
}
@Composable
fun TagAndComposable(navController: NavHostController, tag: String){

    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {

                },
                colors = ButtonColors(
                    containerColor = themeColors().background,
                    contentColor = themeColors().text,
                    disabledContentColor = themeColors().text,
                    disabledContainerColor = themeColors().background
                ),
                modifier = Modifier.border(
                    border = BorderStroke(1.dp, themeColors().border),
                    shape = CircleShape
                )
            ) {
                Text(
                    text = "Inspiration",
                    color = themeColors().text,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.wrapContentSize()
                )
            }


            Button(onClick = {
                navController.navigate("SavedDetail")
            }) {
                Text("View All", color = themeColors().text)
            }
        }
        Row {
            QuoteBoxBigger("life is hard but iam get through it", "Prawin")
            Spacer(modifier = Modifier.width(10.dp))
            QuoteBoxBigger("life is hard but iam get through it", "Prawin")

        }
        Spacer(modifier = Modifier.height(25.dp))
    }
}
@Composable
fun QuoteBoxBigger(savedQuote: String, savedQuoteAuthor: String){
    Column (
        modifier = Modifier.width(150.dp)
            .border(
                border = BorderStroke(1.dp, themeColors().text),
                shape = RoundedCornerShape(5.dp)
            ).padding(20.dp)
    ){
        Text(modifier= Modifier.wrapContentSize(), text=savedQuote, color = themeColors().text, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(15.dp))
        Text(modifier= Modifier.wrapContentSize(), text=savedQuoteAuthor, color=themeColors().lightText, fontSize = 15.sp)
    }
}