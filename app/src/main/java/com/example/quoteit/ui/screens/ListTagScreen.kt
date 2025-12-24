package com.example.quoteit.ui.screens

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quoteit.R
import com.example.quoteit.api.NetworkResponse
import com.example.quoteit.data.TagsItem
import com.example.quoteit.db.tag.TagEntity
import com.example.quoteit.ui.theme.themeColors
import com.example.quoteit.viewModels.TagsViewModel

@Composable
fun ListTagScreen(tagsViewModel: TagsViewModel){
    val uiData =  tagsViewModel.uiState.collectAsState().value
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiTagData by tagsViewModel.tagsFlow.collectAsState()


    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            tagsViewModel.getListOfTags();
        }
    }
    Column (
        modifier = Modifier.fillMaxSize()
            .background(color = themeColors().background)
            .padding(10.dp).systemBarsPadding()
    ){

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


        Spacer(modifier = Modifier.height(50.dp))

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
                        if(!marked.value) tagsViewModel.unMarkTheTag(tags[index].id)
                        else tagsViewModel.markTheTag(tags[index])
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
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(tags[index].img)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.error_icon),
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

