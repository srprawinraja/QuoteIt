package com.example.quoteit.ui.screens

import android.util.Log
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.quoteit.R
import com.example.quoteit.db.saved.SavedQuoteEntity
import com.example.quoteit.ui.components.QuoteComponentBox
import com.example.quoteit.ui.theme.themeColors
import com.example.quoteit.viewModels.SavedViewModel

@Composable
fun SavedScreen(navController: NavHostController, savedScreenViewModel: SavedViewModel) {
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

            Text(text = stringResource( R.string.saved), color = themeColors().text, modifier = Modifier.padding(start = 25.dp), fontSize = 30.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Column (
                modifier = Modifier.fillMaxSize()
            ){
                savedScreenViewModel.getSavedTags().forEach { tag->
                    TagAndComposable(navController, tag, savedScreenViewModel)
                }
            }
        }
    }
}
@Composable
fun TagAndComposable(
    navController: NavHostController,
    tag: String,
    savedScreenViewModel: SavedViewModel
){
    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end=20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .border(
                        border = BorderStroke(1.dp, themeColors().border),
                        shape = CircleShape
                    )
                    .background(themeColors().background, CircleShape)
                    .padding(15.dp).wrapContentSize()
            ) {
                Text(
                    text = tag,
                    color = themeColors().text,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Button(onClick = {
                navController.navigate("SavedDetail/$tag")},
                modifier = Modifier.background(themeColors().background),
            ) {
                Text(stringResource(R.string.view_all), color = themeColors().text)
            }

        }

// 77 characters

        Spacer(modifier = Modifier.height(15.dp))
        LazyRow (
            modifier = Modifier.fillMaxWidth().padding(start = 25.dp)
        ){
            items(savedScreenViewModel.getTagBasedSavedQuotes(tag)){ item->
                QuoteComponentBox(item){
                    navController.navigate("Share/${item.savedQuote}")
                }
                Spacer(modifier = Modifier.width(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

    }
}
