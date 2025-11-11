package com.example.quoteit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quoteit.R
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.data.Quote
import com.example.quoteit.data.TagsItem
import com.example.quoteit.ui.theme.ShowQuote
import com.example.quoteit.viewModels.TagsViewModel


data class Tag(val id: Int, val name: String, val img: Int)
val card1:Tag = Tag(1, "power", R.drawable.love_icon)
val card2:Tag = Tag(2, "power", R.drawable.motivational_icon)

val tags = listOf(
    card1,
    card2
)
@Composable
fun ListTagScreen(tagsViewModel: TagsViewModel){
    val uiData: NetworkResponse<List<TagsItem>> = tagsViewModel.uiState.collectAsState().value

    Column (
        modifier = Modifier.fillMaxSize()
            .background(color = Color(0xFF10161B))
    ){
        Spacer(modifier = Modifier.height(20.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                "Tags",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

        }
        Spacer(modifier = Modifier.height(30.dp))
        when (val result = uiData) {
            is NetworkResponse.Success -> {
                ShowListOfTags(uiData.data)
            }

            is NetworkResponse.LoadingQuote -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Error  -> {
                Column (
                    modifier = Modifier.fillMaxSize()
                ){
                    Image(modifier = Modifier.height(50.dp).width(50.dp), painter = painterResource(R.drawable.error_icon), contentDescription = "error")
                }
            }

            else -> {

            }
        }
    }
}
@Composable
fun ShowListOfTags(tags: List<TagsItem>){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Or GridCells.Adaptive(100.dp)
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count=tags.size,
        ) { index ->
            Row(){
                Card (
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    colors = CardColors(
                        containerColor = Color(0xFF293540),
                        contentColor = Color.White,
                        disabledContainerColor =Color.Red,
                        disabledContentColor = Color.Red
                    ),

                    ){
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Image(
                            modifier = Modifier.width(50.dp).height(50.dp),
                            painter = painterResource(tags[index].img),
                            contentDescription = "",
                        )
                        Text(text = tags[index].name, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}