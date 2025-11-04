package com.example.smalltalk

import android.R
import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.smalltalk.ui.theme.HomeScreen
import com.example.smalltalk.ui.theme.SmallTalkTheme
import com.example.smalltalk.utils.SharedPreferenceUtility
import com.example.smalltalk.viewModels.QuoteViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val quoteViewModel: QuoteViewModel  by viewModels()
        enableEdgeToEdge()
        setContent {
            SmallTalkTheme {
                HomeScreen(quoteViewModel, this)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SmallTalkTheme {
    }
}