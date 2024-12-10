package com.example.foodorderappcompose.view.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.foodorderappcompose.datastore.AppDatastore
import com.example.foodorderappcompose.ui.theme.MainColor
import com.example.foodorderappcompose.view.home.getDrawableResId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPage(food: Food, foodListSize: Int) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val datastore = AppDatastore.getInstance(context)
    val foodOrderCountMap = remember { mutableStateMapOf<Int, MutableIntState>() }
    val selectedFoodOrderCount = remember { mutableIntStateOf(0) }


    LaunchedEffect(key1 = true) {
        CoroutineScope(Dispatchers.Main).launch {
            for (foodId in 1..foodListSize) {
                foodOrderCountMap[foodId] = mutableIntStateOf(datastore.readOrderCount(foodId))
            }
            selectedFoodOrderCount.intValue = datastore.readOrderCount(food.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = food.name, fontSize = 25.sp, color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainColor,
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = MainColor,
                    contentColor = Color.White,
                    action = {
                        IconButton(onClick = { snackbarData.dismiss() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                ) {
                    Text(snackbarData.visuals.message, color = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(id = getDrawableResId(food.image)),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
            Text(text = "${food.price} ₺", fontSize = 30.sp, color = Color.Black, modifier = Modifier.padding(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(painterResource(id = R.drawable.restaurant), contentDescription = null)
                Text(
                    text = "${selectedFoodOrderCount.intValue} kez sipariş edildi",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    modifier = Modifier.size(220.dp, 60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainColor,
                        contentColor = Color.White
                    ),
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {

                            foodOrderCountMap[food.id]?.let { orderCount ->
                                handleClick(food.id, orderCount, datastore)
                                selectedFoodOrderCount.intValue = orderCount.intValue
                            }

                            snackbarHostState.showSnackbar(
                                message = "${food.name} siparişiniz alındı",
                                actionLabel = null, //you can use this if you just want to show string action instead of icon (and delete action method of snackbarHost)
                                duration = SnackbarDuration.Short
                            )
                        }
                    }) {
                    Text(text = "Sipariş Ver", fontSize = 20.sp, color = Color.White)
                }
                Box(Modifier.size(20.dp))
            }
        }
    }
}

suspend fun handleClick(foodId: Int, orderCount: MutableIntState, datastore: AppDatastore) {
    var dsOrderCount = datastore.readOrderCount(foodId)
    orderCount.intValue = ++dsOrderCount
    datastore.writeOrderCount(foodId, dsOrderCount)
}