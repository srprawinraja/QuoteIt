package com.prawin.quoteit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.messaging.messaging
import com.prawin.quoteit.factory.QuoteServiceFactory
import com.prawin.quoteit.ui.screens.AuthErrorScreen
import com.prawin.quoteit.ui.screens.ListTagScreen
import com.prawin.quoteit.ui.screens.QuoteShow
import com.prawin.quoteit.ui.screens.SavedDetailScreen
import com.prawin.quoteit.ui.screens.SavedScreen
import com.prawin.quoteit.ui.screens.HomeScreen
import com.prawin.quoteit.ui.theme.QuoteItTheme
import com.prawin.quoteit.viewModels.HomeViewModel
import com.prawin.quoteit.viewModels.QuoteShowViewModel
import com.prawin.quoteit.viewModels.SavedDetailViewModel
import com.prawin.quoteit.viewModels.SavedViewModel
import com.prawin.quoteit.viewModels.TagsViewModel


class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"
    private lateinit var auth: FirebaseAuth
    var showErrorScreen by mutableStateOf(false)

    fun signIn(){
        auth = Firebase.auth
        if(auth.currentUser==null) {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        showErrorScreen = true
                        Log.d(TAG, "signInAnonymously:success")
                        val user = auth.currentUser

                    } else {
                        // If sign in fails, display a message to the user.
                        showErrorScreen = false
                        Log.w(TAG, "signInAnonymously:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }


    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser!=null){
            showErrorScreen=false
        }

    }
    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        signIn()
        Firebase.messaging.subscribeToTopic("daily_quotes")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d(TAG, msg)
            }

        setContent {
            QuoteItTheme {
                val homeViewModel: HomeViewModel by viewModels {
                    QuoteServiceFactory(this)
                }
                val quoteShowViewModel: QuoteShowViewModel by viewModels {
                    QuoteServiceFactory(this)
                }
                val tagsViewModel: TagsViewModel by viewModels {
                    QuoteServiceFactory(this)
                }
                val savedViewModel: SavedViewModel by viewModels {
                    QuoteServiceFactory(this)
                }
                val savedDetailViewModel: SavedDetailViewModel by viewModels() {
                    QuoteServiceFactory(this)
                }
                if(showErrorScreen){
                    auth.uid?.let { uId->
                        AppNavigation(uId, homeViewModel, quoteShowViewModel, tagsViewModel, savedViewModel, savedDetailViewModel)

                    }
                } else {
                    AuthErrorScreen {
                        signIn()
                    }
                }
            }
        }

    }
}

@Composable
fun AppNavigation(
    uId: String,
    homeViewModel: HomeViewModel,
    quoteShowViewModel: QuoteShowViewModel,
    tagsViewModel: TagsViewModel,
    savedViewModel: SavedViewModel,
    savedDetailViewModel: SavedDetailViewModel
){
    val navController = rememberNavController()
    NavHost(navController, startDestination = "Home" ){
        composable("Home"){
            HomeScreen(
                navController, homeViewModel, uId
            )
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
//    HomeScreen(
//        navController =  NavHostController(LocalContext.current),
//        homeViewModel = HomeViewModel(
//            contextHelper = ContextHelper(
//                context = LocalContext.current
//            ),
//            sharedPreferenceHelper = SharedPreferenceHelper(
//                context = LocalContext.current
//            ),
//            gsonHelper = GsonHelper(),
//            tagRepository = TagRepository(
//                context = LocalContext.current
//            ),
//            savedQuoteRepository = SavedQuoteRepository(
//                context = LocalContext.current
//            )
//        )
//    )
   // SavedScreen(navController = NavHostController(LocalContext.current), SavedViewModel(SavedQuoteRepository(LocalContext.current)))
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

