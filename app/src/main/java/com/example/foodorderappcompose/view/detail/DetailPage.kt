package com.example.foodorderappcompose.view.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodorderappcompose.R
import com.example.foodorderappcompose.data.Food
import com.example.foodorderappcompose.ui.theme.MainColor
import com.example.foodorderappcompose.view.home.getDrawableResId
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPage(navController: NavController, food: Food) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = food.name)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainColor,
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) { snackbarData ->
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
        } }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
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
                    .size(200.dp)
                    .clip(CircleShape)
            )
            Text(text = "${food.price} ₺", fontSize = 30.sp, color = Color.Black)
            Button(
                modifier = Modifier.size(220.dp, 60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainColor,
                    contentColor = Color.White
                ),
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "${food.name} siparişiniz alındı",
                            actionLabel = null, //you can use this if you just want to show string action instead of icon (and delete action method of snackbarHost)
                            duration = SnackbarDuration.Short
                        )
                        navController.popBackStack()
                    }
            }) {
                Text(text = "Sipariş Ver", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}