package com.example.foodorderappcompose.view.home

import android.net.Uri
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {

    val homeViewModel: HomeViewModel = viewModel()
    val foodList = homeViewModel.foodList.observeAsState(listOf())
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValues)
        ) {
            LazyColumn {
                val currentFoodList = foodList.value
                if (currentFoodList.isNotEmpty()) {
                    itemsIndexed(currentFoodList) { index, food ->
                        val cardColor = colorList.getOrNull(index % colorList.size) ?: Color.Gray
                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = cardColor,
                            ),
                            modifier = Modifier
                                .padding(all = 5.dp)
                                .fillMaxWidth()
                                .background(color = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.clickable {
                                    val json =  Uri.encode(Gson().toJson(food))
                                    navController.navigate("detailPage/$json/${foodList.value.size}")
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(all = 10.dp)
                                        .fillMaxWidth()
                                ) {
                                    GlideImage(
                                        imageModel = food.image,
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
                                            Text(
                                                text = food.name.toString(),
                                                fontSize = 20.sp,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.size(20.dp))
                                            Text(
                                                text = "${food.price} ₺",
                                                fontSize = 20.sp,
                                                color = Color.Black
                                            )
                                        }
                                        Icon(
                                            painterResource(id = R.drawable.arrow),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}