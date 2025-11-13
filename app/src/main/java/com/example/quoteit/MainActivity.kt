package com.example.quoteit

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quoteit.factory.QuoteServiceFactory
import com.example.quoteit.ui.screens.ListTagScreen
import com.example.quoteit.ui.screens.QuoteShow
import com.example.quoteit.ui.theme.HomeScreen
import com.example.quoteit.ui.theme.SmallTalkTheme
import com.example.quoteit.viewModels.QuoteShowViewModel
import com.example.quoteit.viewModels.HomeViewModel
import com.example.quoteit.viewModels.TagsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            SmallTalkTheme {
                HomeScreenPreview()
                val homeViewModel: HomeViewModel  by viewModels(){
                    QuoteServiceFactory(this)
                }
                val quoteShowViewModel: QuoteShowViewModel  by viewModels(){
                    QuoteServiceFactory(this)
                }
                val tagsViewModel: TagsViewModel  by viewModels(){
                    QuoteServiceFactory(this)
                }
                AppNavigation(homeViewModel, quoteShowViewModel, tagsViewModel)
            }
        }
    }
}
@Composable
fun AppNavigation(homeViewModel: HomeViewModel, quoteShowViewModel: QuoteShowViewModel, tagsViewModel: TagsViewModel){
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
            ListTagScreen(tagsViewModel)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
   /* ShowListOfTags(listOf(
        TagsItem(
            _id = "1",
            name="Business",
            slug = "dsa",
            img =  R.drawable.love_icon
        ),
        TagsItem(
            _id = "1",
            name="Motivational",
            slug = "dsa",
            img =  R.drawable.motivational_icon
        )
    ))*/
   // val context = LocalContext.current
    //val app = context.applicationContext as Application
 /*HomeScreen(
       navController = NavHostController(LocalContext.current), HomeViewModel(
           ContextHelper(LocalContext.current),
           SharedPreferenceHelper(LocalContext.current),
           GsonHelper(),
           TagDatabaseService(application = null)
       ))
  // QuoteShow("The fact that you aren't where you want to be should be enough motivation", QuoteShowViewModel(ContextHelper(LocalContext.current)))
    //ListTagScreen(TagsViewModel(TagDatabaseService(app), CacheImageHelper()))
    // class TagsViewModel (val tagDatabaseService: TagDatabaseService, val cacheImageHelper: CacheImageHelper): ViewModel() {
*/
}

