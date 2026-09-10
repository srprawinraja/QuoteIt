package com.prawin.quoteit

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.prawin.quoteit.api.NetworkResponse
import com.prawin.quoteit.factory.QuoteServiceFactory
import com.prawin.quoteit.ui.screens.AuthErrorScreen
import com.prawin.quoteit.ui.screens.HomeScreen
import com.prawin.quoteit.ui.screens.ListTagScreen
import com.prawin.quoteit.ui.screens.LoadingScreen
import com.prawin.quoteit.ui.screens.QuoteShow
import com.prawin.quoteit.ui.screens.SavedDetailScreen
import com.prawin.quoteit.ui.screens.SavedScreen
import com.prawin.quoteit.ui.theme.QuoteItTheme
import com.prawin.quoteit.viewModels.HomeViewModel
import com.prawin.quoteit.viewModels.QuoteShowViewModel
import com.prawin.quoteit.viewModels.SavedDetailViewModel
import com.prawin.quoteit.viewModels.SavedViewModel
import com.prawin.quoteit.viewModels.TagsViewModel
import com.prawin.quoteit.worker.DailyNotificationWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"
    val CHANNEL_ID = "SHOW_QUOTE"
    private lateinit var auth: FirebaseAuth
    var intialScreenState by mutableStateOf<NetworkResponse<String>>(NetworkResponse.Loading)


    fun signIn(){
        auth = Firebase.auth

        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    currentUser?.let {
                        intialScreenState = NetworkResponse.Success(it.uid)
                    }
                    Log.d(TAG, "signInAnonymously:success")

                } else {
                    // If sign in fails, display a message to the user.
                    intialScreenState = NetworkResponse.Error("")
                }
            }
    }


    public override fun onStart() {
        super.onStart()
        //showNotification()

        val currentUser = auth.currentUser
        if(currentUser!=null){
            // signedin
           currentUser.let {
               intialScreenState= NetworkResponse.Success(it.uid)
           }
        } else {
            // not signed in
            signIn()
        }

    }

    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request =
            PeriodicWorkRequestBuilder<DailyNotificationWorker>(
                10, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

        WorkManager
            .getInstance(application)
            .enqueue(request)


        enableEdgeToEdge()
        signIn()


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
                when(val result = intialScreenState){
                    is NetworkResponse.Success<String> -> {
                        AppNavigation(result.data, homeViewModel, quoteShowViewModel, tagsViewModel, savedViewModel, savedDetailViewModel)
                    }
                    is NetworkResponse.Error -> {
                        AuthErrorScreen {
                            signIn()
                        }
                    }
                    else -> {
                        LoadingScreen()
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

