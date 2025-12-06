package com.example.quoteit.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quoteit.R
import com.example.quoteit.ui.theme.themeColors
import com.example.quoteit.viewModels.SavedDetailViewModel

@Composable
fun SavedDetailScreen(navController: NavHostController, savedDetailViewModel: SavedDetailViewModel) {
    Column (
        modifier = Modifier.
        fillMaxSize().
        background(color = themeColors().background).padding(10.dp)
    ){
        IconButton(
            onClick = {
                navController.navigate("Home")
            }
        ) {
            Icon(painter = painterResource(R.drawable.left_icon), contentDescription = "left button")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "Saved",
            color = themeColors().text,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
        )
        TagRow(navController)
        TagRow(navController)
        TagRow(navController)

    }
}

@Composable
fun TagRow(navController: NavHostController){
    Spacer(modifier = Modifier.height(30.dp))

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
            modifier = Modifier.wrapContentSize(),
            text = "Inspiration",
            color = themeColors().text,
            fontSize = 15.sp
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row {
        QuoteComponentBox("life got hard but iam harder than it", "prawin", navController)
        QuoteComponentBox("life got hard but iam harder than it", "prawin", navController)
        QuoteComponentBox("life got hard but iam harder than it", "prawin", navController)
        QuoteComponentBox("life got hard but iam harder than it", "prawin", navController)

    }
}

@Composable
fun QuoteComponentBox(quote: String, author: String, navController: NavHostController){
    Column (
        modifier = Modifier.width(150.dp)
            .border(
                border = BorderStroke(1.dp, themeColors().text),
                shape = RoundedCornerShape(5.dp)
            ).padding(20.dp).clickable(onClick = {
                navController.navigate("Share/$quote")
            })
    ){
        Text(modifier= Modifier.wrapContentSize(), text="life got hard but iam harder than it", color = themeColors().text, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(15.dp))
        Text(modifier= Modifier.wrapContentSize(), text="Inspiration", color=themeColors().lightText, fontSize = 15.sp)
    }
}