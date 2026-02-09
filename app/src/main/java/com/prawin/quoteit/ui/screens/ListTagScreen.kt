package com.prawin.quoteit.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

import com.prawin.quoteit.R
import com.prawin.quoteit.api.NetworkResponse
import com.prawin.quoteit.data.TagsItem
import com.prawin.quoteit.db.tag.TagEntity
import com.prawin.quoteit.ui.theme.themeColors
import com.prawin.quoteit.viewModels.TagsViewModel

@Composable
fun ListTagScreen(tagsViewModel: TagsViewModel, navController: NavHostController){
    val uiData =  tagsViewModel.uiState.collectAsState().value
    val uiTagData by tagsViewModel.tagsFlow.collectAsState()

    Column (
        modifier = Modifier.fillMaxSize()
            .background(color = themeColors().background)
            .padding(10.dp).systemBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Tags",
                color = themeColors().text,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

        }
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            when (val result = uiData) {
                is NetworkResponse.Success -> {
                    ShowListOfTags(tagsViewModel, result.data, uiTagData)
                }

                is NetworkResponse.Error -> {
                    ErrorPage(uiData.message)
                }

                is NetworkResponse.Loading -> {
                    LoadingPage()
                }

                else -> {}
            }

            Box(
                modifier = Modifier.fillMaxSize().padding(bottom = 70.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        tagsViewModel.commitChanges()
                        navController.popBackStack()
                    },
                    colors = ButtonColors(
                        containerColor = colorResource(R.color.green),
                        contentColor = themeColors().text,
                        disabledContentColor = themeColors().text,
                        disabledContainerColor = colorResource(R.color.green)
                    ),
                    modifier = Modifier.border(
                        border = BorderStroke(1.dp, themeColors().lightBorderColor),
                        shape = CircleShape
                    ),


                    ) {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        text = "save",
                        color = themeColors().text,
                        fontSize = 25.sp
                    )
                }

            }
        }




    }


}
@Composable
fun ShowListOfTags(tagsViewModel: TagsViewModel, tags: List<TagsItem>, uiTagData: List<TagEntity>){
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
                    remember { mutableStateOf(tagsViewModel.isTagMarked(tags[index].slug)) }
                Card(
                    modifier = Modifier.fillMaxWidth().height(150.dp).clickable(onClick = {
                        marked.value = !marked.value

                        if(marked.value) tagsViewModel.addSelectedTag(tags[index])
                        else tagsViewModel.removeSelectedTag(tags[index])
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
                        AsyncImage(
                            model = tags[index].img,
                            contentDescription = "Quote Image",
                            modifier =Modifier.width(50.dp).height(50.dp)
                        )
                        Text(text = tags[index].tag, fontSize = 20.sp)
                    }
            }
        }

    }
}

@Composable
fun ErrorPage(message: String){
    Column (
       modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            message,
            color = themeColors().text,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoadingPage(){
    Column (
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ){
        CircularProgressIndicator()
    }
}

