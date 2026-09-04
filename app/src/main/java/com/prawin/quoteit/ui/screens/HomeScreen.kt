package com.prawin.quoteit.ui.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.prawin.quoteit.R
import com.prawin.quoteit.ui.theme.themeColors
import com.prawin.quoteit.api.NetworkResponse
import com.prawin.quoteit.data.model.Quote
import com.prawin.quoteit.ui.components.DialogComponent
import com.prawin.quoteit.ui.components.RationaleDialogComponent
import com.prawin.quoteit.viewModels.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    uId: String
) {

    val context = LocalContext.current
    val uiData = homeViewModel.uiState.collectAsState().value
    val uiTagData by homeViewModel.markedTagsFlow.collectAsState()
    val uiStreakData by homeViewModel.uiStreakState.collectAsState()

    val marked by homeViewModel.marked
    var showRationaleDialogComponent by remember { mutableStateOf(false) }
    var showDialogComponent by remember { mutableStateOf(false) }

    val activity = context as? Activity

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            showDialogComponent = true
        }
    }


    LaunchedEffect(Unit) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val permissionGranted =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

            if (!permissionGranted) {

                val shouldShowRationale =
                    activity?.shouldShowRequestPermissionRationale(
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == true

                if (shouldShowRationale) {
                    showRationaleDialogComponent = true
                } else {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                homeViewModel.getStreak(uId)
            }
        }
    }

    if (showDialogComponent) {
        DialogComponent(
            title = "Notifications are turned off",
            description = "You won't receive notifications from this app. You can enable notifications anytime in your device settings.",
            onDismiss = { showDialogComponent = false }
        )
    }
    if (showRationaleDialogComponent) {
        RationaleDialogComponent(
            title = "Don't miss your daily quotes",
            description = "Allow notifications to receive your daily quotes and a little inspiration throughout your day.",
            onConfirm = {
                showRationaleDialogComponent = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        ) {
            showRationaleDialogComponent = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = themeColors().background)
            .padding(30.dp)
            .systemBarsPadding(),
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.quote_left_side_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = (-4).dp, y = (-12).dp)
                        .size(20.dp),
                )
                Text(
                    text = stringResource(R.string.app_name),
                    color = themeColors().text,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif
                )
                Image(
                    painter = painterResource(R.drawable.quote_right_side_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = 4.dp, y = (-12).dp)
                        .size(20.dp)
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .background(color = themeColors().surface, shape = CircleShape)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when(val result = uiStreakData){
                   is NetworkResponse.Success -> {
                       Image(
                           painter = painterResource(R.drawable.ic_fire_logo),
                           contentDescription = null,
                           modifier = Modifier.size(24.dp)
                       )
                       Spacer(modifier = Modifier.width(4.dp))
                       Text(
                           text = result.data.toString(),
                           color = themeColors().text,
                           fontSize = 18.sp,
                           fontWeight = FontWeight.Bold
                       )
                    }
                    else -> {}
                }

            }

        }
        Spacer(modifier = Modifier.height(80.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    homeViewModel.selectedId = 0
                    homeViewModel.updateTodayQuote()
                },
                colors = ButtonColors(
                    containerColor = themeColors().background,
                    contentColor = themeColors().text,
                    disabledContentColor = themeColors().text,
                    disabledContainerColor = themeColors().background
                ),
                modifier = Modifier.then(
                    if (homeViewModel.selectedId == 0) {
                        Modifier.border(
                            border = BorderStroke(1.dp, themeColors().border),
                            shape = CircleShape
                        )
                    } else Modifier.border(
                        border = BorderStroke(1.dp, themeColors().lightBorderColor),
                        shape = CircleShape
                    )
                )
            ) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = stringResource(R.string.today),
                    color = themeColors().text,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            uiTagData.map { tag ->
                val mark = remember { mutableStateOf(true) }
                if (mark.value) {
                    Button(
                        onClick = {
                            homeViewModel.selectedId = tag.id
                            homeViewModel.updateSelectedTagQuote(tag.slug)
                        },
                        colors = ButtonColors(
                            containerColor = themeColors().background,
                            contentColor = themeColors().text,
                            disabledContentColor = themeColors().text,
                            disabledContainerColor = themeColors().background
                        ),
                        modifier = Modifier.then(
                            if (homeViewModel.selectedId == tag.id) {
                                Modifier.border(
                                    border = BorderStroke(1.dp, themeColors().border),
                                    shape = CircleShape
                                )// Color(0xFF2C2F36)
                            } else Modifier.border(
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
                        if (homeViewModel.selectedId == tag.id) {
                            Icon(
                                modifier = Modifier.clickable(onClick = {
                                    homeViewModel.updateTag(tag)
                                }),
                                painter = painterResource(R.drawable.cancel_icon),
                                contentDescription = stringResource(R.string.content_desc_cancel_icon),
                                tint = themeColors().text
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
            Icon(
                painter = painterResource(R.drawable.add_icon),
                contentDescription = stringResource(R.string.content_desc_add),
                tint = themeColors().text,
                modifier = Modifier
                    .size(45.dp)
                    .clickable(onClick = {
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
                    result.data.tagName,
                    marked
                )
            }

            is NetworkResponse.ErrorQuote -> {
                ShowQuote(
                    navController,
                    homeViewModel,
                    null,
                    result.data.quote,
                    result.data.author,
                    result.data.tagName,
                    marked
                )
            }

            is NetworkResponse.LoadingQuote -> {
                ShowQuote(
                    navController,
                    homeViewModel,
                    null,
                    result.data.quote,
                    result.data.author,
                    result.data.tagName,
                    marked
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
    tag: String,
    marked: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(R.drawable.quote_left_side_icon),
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
        Text(
            text = quote,
            fontSize = 32.sp,
            color = themeColors().text,
            fontFamily = FontFamily.Serif,
            lineHeight = 40.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(R.drawable.quote_right_side_icon),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            modifier = Modifier.wrapContentSize(),
            text = author.uppercase(),
            color = themeColors().text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.wrapContentSize(),
            text = tag,
            color = themeColors().lightText,
            fontSize = 16.sp
        )
    }
    Spacer(modifier = Modifier.height(30.dp))
    MiddleRowButtons(navController, uiData, homeViewModel, quote, marked)
}

@Composable
fun MiddleRowButtons(
    navController: NavHostController,
    uiData: NetworkResponse.Success<Quote>?,
    homeViewModel: HomeViewModel,
    quote: String,
    marked: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
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
                contentDescription = stringResource(R.string.content_desc_save),
                tint = themeColors().text,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.width(30.dp))
        IconButton(
            onClick = {
                if (!marked) {
                    uiData?.let {
                        homeViewModel.saveQuote(
                            uiData.data.documentId,
                            uiData.data.quote,
                            uiData.data.author,
                            uiData.data.slugs[0]
                        )
                        homeViewModel.changeMarked(true)
                    }
                } else {
                    uiData?.let {
                        homeViewModel.deleteQuote(uiData.data.documentId)
                        homeViewModel.changeMarked(true)
                        homeViewModel.changeMarked(false)
                    }
                }
            },
            modifier = Modifier
                .size(40.dp)
                .background(color = themeColors().surface, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(
                    if (marked)
                        R.drawable.save_bookmark_icon
                    else R.drawable.unsave_bookmark_icon
                ),
                contentDescription = stringResource(R.string.content_desc_save),
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
                contentDescription = stringResource(R.string.content_desc_share),
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
