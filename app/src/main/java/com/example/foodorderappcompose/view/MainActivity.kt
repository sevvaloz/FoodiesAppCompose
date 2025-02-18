package com.example.foodorderappcompose.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.foodorderappcompose.data.Food
import com.example.foodorderappcompose.handler.NotificationWorker
import com.example.foodorderappcompose.ui.theme.FoodOrderAppComposeTheme
import com.example.foodorderappcompose.view.detail.DetailPage
import com.example.foodorderappcompose.view.home.HomePage
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodOrderAppComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigator()
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (NotificationManagerCompat.from(this).areNotificationsEnabled().not()) {
                //permission not granted, request permission from user
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        //user accepted permission request
                        setupPeriodicNotification(this) //set notification
                    } else {
                        //user not accepted permission request
                    }
                }.launch(android.Manifest.permission.POST_NOTIFICATIONS)

            } else {
                //permission granted, set notification
                setupPeriodicNotification(this)
            }

        } else {
            //permission is not required before Android 13
            setupPeriodicNotification(this) //set notification
        }

    }
}

private fun setupPeriodicNotification(context: Context) {
    val periodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(10, TimeUnit.DAYS)
        .setInitialDelay(15, TimeUnit.MINUTES)
        .build()

    WorkManager.getInstance(context)
        .enqueueUniquePeriodicWork(
            "NotificationWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
}

@Composable
fun Navigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "homePage") {
        composable("homePage") {
            HomePage(navController = navController)
        }
        composable(
            "detailPage/{FOOD}/{FOOD_LIST_SIZE}",
            arguments = listOf(
                navArgument("FOOD") { type = NavType.StringType},
                navArgument("FOOD_LIST_SIZE") { type = NavType.IntType}
            )
        ) {
            val json = it.arguments?.getString("FOOD")
            val food = Gson().fromJson(json, Food::class.java)
            val foodListSize = it.arguments?.getInt("FOOD_LIST_SIZE") ?: 0
            DetailPage(food = food, foodListSize = foodListSize)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    FoodOrderAppComposeTheme {
        Navigator()
    }
}