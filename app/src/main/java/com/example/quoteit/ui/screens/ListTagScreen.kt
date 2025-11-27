package com.example.quoteit.ui.screens

import android.util.Log
import android.widget.ProgressBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quoteit.R
import com.example.quoteit.db.tag.TagEntity
import com.example.quoteit.ui.theme.themeColors
import com.example.quoteit.viewModels.TagsViewModel

@Composable
fun ListTagScreen(tagsViewModel: TagsViewModel){
    val uiData by tagsViewModel.tagsFlow.collectAsState()


    Column (
        modifier = Modifier.fillMaxSize()
            .background(color = themeColors().background)
            .padding(10.dp)
    ){
        Spacer(modifier = Modifier.height(40.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                "Tags",
                color = themeColors().text,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

        }
        Spacer(modifier = Modifier.height(20.dp))

       ShowListOfTags(tagsViewModel, uiData)

        Spacer(modifier = Modifier.height(50.dp))

    }
}
@Composable
fun ShowListOfTags(tagsViewModel: TagsViewModel, tags: List<TagEntity>){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Or GridCells.Adaptive(100.dp)
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count=tags.size,
        ) { index ->
                val marked: MutableState<Boolean> =
                    remember { mutableStateOf(tags[index].tagMarked) }
                Card(
                    modifier = Modifier.fillMaxWidth().height(150.dp).clickable(onClick = {
                        marked.value = !marked.value
                        tagsViewModel.changeTagMark(tags[index].id, marked.value)
                    }),
                    colors = CardColors(
                        containerColor = themeColors().surface,
                        contentColor = themeColors().text,
                        disabledContainerColor = themeColors().surface,
                        disabledContentColor = themeColors().text,
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (marked.value) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Image(
                                    modifier = Modifier.wrapContentSize().padding(end = 10.dp),
                                    painter = painterResource(R.drawable.tick_icon),
                                    contentDescription = ""
                                )
                            }
                        }
                        Image(
                            modifier = Modifier.width(50.dp).height(50.dp),
                            painter = painterResource(tags[index].tagImg),
                            contentDescription = "",
                        )
                        Text(text = tags[index].tagName, fontSize = 20.sp)
                    }

            }
        }
    }
}