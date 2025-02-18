package com.example.foodorderappcompose.view.detail

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodorderappcompose.R
import com.example.foodorderappcompose.data.Food
import com.example.foodorderappcompose.datastore.AppDatastore
import com.example.foodorderappcompose.ui.theme.MainColor
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPage(food: Food?, foodListSize: Int) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val activity = context as? Activity
    val datastore = AppDatastore.getInstance(context)
    val foodOrderCountMap = remember { mutableStateMapOf<Int, MutableIntState>() }
    val selectedFoodOrderCount = remember { mutableIntStateOf(0) }
    var toggled by remember { mutableStateOf(false) }
    val animatedSize by animateSizeAsState(
        targetValue = if (toggled) Size(200f, 50f) else Size(220f, 60f),
        animationSpec = tween(durationMillis = 100)
    )

    LaunchedEffect(key1 = true) {
        CoroutineScope(Dispatchers.Main).launch {
            for (foodId in 1..foodListSize) {
                foodOrderCountMap[foodId] = mutableIntStateOf(datastore.readOrderCount(foodId))
            }
            food?.id?.let { foodId ->
                datastore.readOrderCount(foodId)
            }?.also { foodOrderCount ->
                selectedFoodOrderCount.intValue = foodOrderCount
            }
        }
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //locked screen orientationto avoid turning into landscape
    }

    DisposableEffect(Unit) {
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED //unlocked
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
                        Text(text = food?.name.toString(), fontSize = 25.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainColor
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = MainColor,
                    action = {
                        IconButton(onClick = { snackbarData.dismiss() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                ) {
                    Text(snackbarData.visuals.message, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            GlideImage(
                imageModel = food?.image,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(0.dp, 400.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Text(
                text = "${food?.price} ₺",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(10.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(painterResource(id = R.drawable.restaurant), tint = MaterialTheme.colorScheme.onSecondary, contentDescription = null)
                Text(
                    text = "${selectedFoodOrderCount.intValue} kez sipariş edildi",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    modifier = Modifier.size(animatedSize.width.dp, animatedSize.height.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainColor
                    ),
                    onClick = {
                        toggled = !toggled

                        CoroutineScope(Dispatchers.Main).launch {

                            delay(100)
                            toggled = !toggled

                            foodOrderCountMap[food?.id]?.let { orderCount ->
                                food?.id?.let { food ->
                                    handleClick(food, orderCount, datastore)
                                }
                                selectedFoodOrderCount.intValue = orderCount.intValue
                            }

                            snackbarHostState.showSnackbar(
                                message = "${food?.name} siparişiniz alındı",
                                actionLabel = null, //you can use this if you just want to show string action instead of icon (and delete action method of snackbarHost)
                                duration = SnackbarDuration.Short
                            )
                        }
                    }) {
                    Text(text = "Sipariş Ver", fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

suspend fun handleClick(foodId: Int, orderCount: MutableIntState, datastore: AppDatastore) {
    var dsOrderCount = datastore.readOrderCount(foodId)
    orderCount.intValue = ++dsOrderCount
    datastore.writeOrderCount(foodId, dsOrderCount)
}