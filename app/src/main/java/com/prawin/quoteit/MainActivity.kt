package com.prawin.quoteit

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prawin.quoteit.db.saved.SavedQuoteRepository
import com.prawin.quoteit.db.tag.TagRepository
import com.prawin.quoteit.factory.QuoteServiceFactory
import com.prawin.quoteit.ui.screens.ListTagScreen
import com.prawin.quoteit.ui.screens.QuoteShow
import com.prawin.quoteit.ui.screens.SavedDetailScreen
import com.prawin.quoteit.ui.screens.SavedScreen
import com.prawin.quoteit.ui.theme.HomeScreen
import com.prawin.quoteit.ui.theme.QuoteItTheme
import com.prawin.quoteit.utils.ContextHelper
import com.prawin.quoteit.viewModels.HomeViewModel
import com.prawin.quoteit.viewModels.QuoteShowViewModel
import com.prawin.quoteit.viewModels.SavedDetailViewModel
import com.prawin.quoteit.viewModels.SavedViewModel
import com.prawin.quoteit.viewModels.TagsViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            QuoteItTheme {
                val homeViewModel: HomeViewModel by viewModels() {
                    QuoteServiceFactory(this)
                }
                val quoteShowViewModel: QuoteShowViewModel by viewModels() {
                    QuoteServiceFactory(this)
                }
                val tagsViewModel: TagsViewModel by viewModels() {
                    QuoteServiceFactory(this)
                }
                val savedViewModel: SavedViewModel by viewModels() {
                    QuoteServiceFactory(this)
                }
                val savedDetailViewModel: SavedDetailViewModel by viewModels() {
                    QuoteServiceFactory(this)
                }
                AppNavigation( homeViewModel, quoteShowViewModel, tagsViewModel, savedViewModel, savedDetailViewModel)
            }
        }

    }
}
@Composable
fun AppNavigation(
    homeViewModel: HomeViewModel,
    quoteShowViewModel: QuoteShowViewModel,
    tagsViewModel: TagsViewModel,
    savedViewModel: SavedViewModel,
    savedDetailViewModel: SavedDetailViewModel
){
    val navController = rememberNavController()
    NavHost(navController, startDestination = "Home" ){
        composable("Home"){
            HomeScreen(navController, homeViewModel)
        }
        composable(
            route= "Share/{quote}",
            arguments = listOf(navArgument("quote") { type = NavType.StringType })
        ){
                backStackEntry ->
            val quote = backStackEntry.arguments?.getString("quote")
            if(quote!=null){
                QuoteShow(quote, quoteShowViewModel)
            }
        }
        composable("Tags"){
            ListTagScreen(tagsViewModel, navController)
        }
        composable("Saved"){
            SavedScreen(navController, savedViewModel)
        }
        composable(
            route= "SavedDetail/{tag}",
            arguments = listOf(navArgument("tag") { type = NavType.StringType })
        ){
                backStackEntry ->
            val tag = backStackEntry.arguments?.getString("tag")
            if(tag!=null){
                SavedDetailScreen(navController, savedDetailViewModel, tag)
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SavedScreen(navController = NavHostController(LocalContext.current), SavedViewModel(SavedQuoteRepository(LocalContext.current)))
  // QuoteShow("The fact that you aren't where you want to be should be enough motivation", QuoteShowViewModel(ContextHelper(LocalContext.current)))
        /* ListTagScreen(
        tagsViewModel = TagsViewModel(
            tagRepository = TagRepository(
                context =  LocalContext.current
            ),
            contextHelper = ContextHelper( LocalContext.current)
        ),
        navController = navController
    )*/
    // class TagsViewModel (val tagDatabaseService: TagDatabaseService, val cacheImageHelper: CacheImageHelper): ViewModel() {
}

