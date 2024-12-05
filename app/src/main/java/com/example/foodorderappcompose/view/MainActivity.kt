package com.example.foodorderappcompose.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodorderappcompose.data.Food
import com.example.foodorderappcompose.ui.theme.FoodOrderAppComposeTheme
import com.example.foodorderappcompose.view.detail.DetailPage
import com.example.foodorderappcompose.view.home.HomePage
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodOrderAppComposeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    Navigator()
                }
            }
        }
    }
}

@Composable
fun Navigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "homePage") {
        composable("homePage") {
            HomePage(navController = navController)
        }
        composable(
            "detailPage/{FOOD}",
            arguments = listOf(navArgument("FOOD") { type = NavType.StringType})
        ) {
            val json = it.arguments?.getString("FOOD")
            val food = Gson().fromJson(json, Food::class.java)
            DetailPage(food)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodOrderAppComposeTheme {
        Navigator()
    }
}