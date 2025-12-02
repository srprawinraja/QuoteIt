package com.example.quoteit.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
        IconButton(
            onClick = {
                navController.navigate("saved")
            }
        ) {
            Icon(painter = painterResource(R.drawable.left_icon), contentDescription = "left button")
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text("Saved")
        Spacer(modifier = Modifier.height(10.dp))
        Column (
            modifier = Modifier.fillMaxSize().horizontalScroll(rememberScrollState())
        ){
            uiSavedData.forEach { it -> TagAndComposable(navController, it.savedTagName, it.savedQuote, it.savedAuthor)
            }
        }

    }
}
@Composable
fun TagAndComposable(navController: NavHostController, tag: String, quote: String, author: String){

    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Inspiration",
                color = themeColors().text,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
            )


            Button(onClick = {
                navController.navigate("SavedDetail")
            }) {
                Text("View All")
            }
        }
        Row {
            quoteBoxBigger()
            Spacer(modifier = Modifier.width(10.dp))
            quoteBoxBigger()
        }
        Spacer(modifier = Modifier.height(25.dp))
    }
}
@Composable
fun quoteBoxBigger(){
    Column (
        modifier = Modifier.width(150.dp)
            .border(
                border = BorderStroke(1.dp, themeColors().text),
                shape = RoundedCornerShape(5.dp)
            ).padding(20.dp)
    ){
        Text(modifier= Modifier.wrapContentSize(), text="life got hard but iam harder than it", color = themeColors().text, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(15.dp))
        Text(modifier= Modifier.wrapContentSize(), text="Inspiration", color=themeColors().lightText, fontSize = 15.sp)
    }
}