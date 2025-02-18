package com.example.foodorderappcompose.view.home

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foodorderappcompose.R
import com.example.foodorderappcompose.ui.theme.MainColor
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {

    val homeViewModel: HomeViewModel = viewModel()
    val foodList = homeViewModel.foodList.observeAsState(listOf())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Foodie's", fontSize = 25.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainColor
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn {
                val currentFoodList = foodList.value
                if (currentFoodList.isNotEmpty()) {
                    itemsIndexed(currentFoodList) { index, food ->
                        val expanded = remember { mutableStateOf(false) }
                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MainColor,
                            ),
                            modifier = Modifier
                                .padding(all = 5.dp)
                                .fillMaxWidth()
                                .animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioNoBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        val json = Uri.encode(Gson().toJson(food))
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
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                            Spacer(modifier = Modifier.size(20.dp))
                                            Text(
                                                text = "${food.price} ₺",
                                                fontSize = 20.sp,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                expanded.value = !expanded.value
                                            }
                                        ) {
                                            Icon(
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                imageVector = if (expanded.value) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                            AnimatedVisibility(
                                visible = expanded.value,
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                Column(
                                    modifier = Modifier.padding(
                                        start = dimensionResource(R.dimen.padding_medium),
                                        top = dimensionResource(R.dimen.padding_small),
                                        bottom = dimensionResource(R.dimen.padding_medium),
                                        end = dimensionResource(R.dimen.padding_medium)
                                    )
                                ) {
                                    Text(
                                        text = food.description.toString(),
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onPrimary
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