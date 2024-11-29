package com.example.foodorderappcompose.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodorderappcompose.R
import com.example.foodorderappcompose.data.Food
import com.example.foodorderappcompose.ui.theme.CardView1
import com.example.foodorderappcompose.ui.theme.CardView2
import com.example.foodorderappcompose.ui.theme.CardView3
import com.example.foodorderappcompose.ui.theme.CardView4
import com.example.foodorderappcompose.ui.theme.CardView5
import com.example.foodorderappcompose.ui.theme.CardView6
import com.example.foodorderappcompose.ui.theme.CardView7
import com.example.foodorderappcompose.ui.theme.CardView8
import com.example.foodorderappcompose.ui.theme.CardView9
import com.example.foodorderappcompose.ui.theme.MainColor
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {

    val foodList = remember { mutableStateListOf<Food>() }

    val colorList = listOf(
        CardView1,
        CardView2,
        CardView3,
        CardView4,
        CardView5,
        CardView6,
        CardView7,
        CardView8,
        CardView9
    )

    LaunchedEffect(key1 = Unit) {

        Log.d("TAG","LaunchedEffect")

        val food1 = Food(1, "Köfte", "kofte", 150.75f)
        val food2 = Food(2, "Pizza", "pizza", 250.00f)
        val food3 = Food(3, "Pide", "pide", 200.95f)
        val food4 = Food(4, "Lahmacun", "lahmacun", 120.25f)
        val food5 = Food(5, "Hamburger", "hamburger", 300.45f)
        val food6 = Food(6, "Sarma", "sarma", 180.00f)
        val food7 = Food(7, "Mantı", "manti", 220.65f)
        val food8 = Food(8, "Çorba", "corba", 90.15f)
        val food9 = Food(10, "Salata", "salata", 110.50f)

        foodList.addAll(listOf(food1, food2, food3, food4, food5, food6, food7, food8, food9))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Foodie's")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainColor,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(paddingValues)
        ) {
            LazyColumn {
                itemsIndexed(foodList) { index, food ->
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = colorList[index],
                        ),
                        modifier = Modifier
                            .padding(all = 5.dp)
                            .fillMaxWidth()
                            .background(color = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.clickable{
                                val json = Gson().toJson(food)
                                navController.navigate("detailPage/$json")
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(all = 10.dp)
                                    .fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = getDrawableResId(food.image)),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.SpaceEvenly,
                                        modifier = Modifier.fillMaxHeight()
                                    ) {
                                        Text(text = food.name, fontSize = 20.sp, color = Color.Black)
                                        Spacer(modifier = Modifier.size(20.dp))
                                        Text(text = "${food.price} ₺", fontSize = 20.sp, color = Color.Black)
                                    }
                                    Icon(painterResource(id = R.drawable.arrow), contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getDrawableResId(image: String): Int {
    return LocalContext.current.resources.getIdentifier(
        image,
        "drawable",
        LocalContext.current.packageName
    )
}