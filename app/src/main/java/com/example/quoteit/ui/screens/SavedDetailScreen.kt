package com.example.quoteit.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quoteit.R
import com.example.quoteit.ui.components.QuoteComponentBox
import com.example.quoteit.ui.theme.themeColors
import com.example.quoteit.viewModels.SavedDetailViewModel

@Composable
fun SavedDetailScreen(navController: NavHostController, savedDetailViewModel: SavedDetailViewModel, tag: String) {
    LazyColumn (
        modifier = Modifier.
        fillMaxSize().
        background(color = themeColors().background).systemBarsPadding()
    ){
        item {
            IconButton(
                onClick = {
                    navController.navigate("Home")
                },
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.left_icon),
                    contentDescription = "left button",
                    tint = themeColors().text,
                    modifier = Modifier.width(30.dp).height(30.dp)
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = tag, color = themeColors().text, modifier = Modifier.padding(start = 25.dp), fontSize = 30.sp)
            Spacer(modifier = Modifier.height(20.dp))
            val itemsList = savedDetailViewModel.getTagBasedSavedQuotes(tag)
            val columns = 2
            val rows = (itemsList.size + columns - 1) / columns  // round up

            Column {
                for (rowIndex in 0 until rows) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (colIndex in 0 until columns) {
                            val itemIndex = rowIndex * columns + colIndex
                            if (itemIndex < itemsList.size) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp)
                                        .aspectRatio(1f) // optional: make square
                                ) {
                                    QuoteComponentBox(itemsList[itemIndex]) {
                                        navController.navigate("Share/${itemsList[itemIndex].savedQuote}")
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f)) // empty space for incomplete row
                            }
                        }
                    }
                }
            }


        }
    }
}



