package com.example.foodorderappcompose.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderappcompose.data.Food
import com.example.foodorderappcompose.repository.FoodRepo

class HomeViewModel: ViewModel() {

    private var foodRepo = FoodRepo()

    private var _foodList = MutableLiveData<List<Food>>()
    val foodList: LiveData<List<Food>>
        get() = _foodList

    init {
        foodRepo.foodListRepo.observeForever { newFoodList ->
            _foodList.value = newFoodList
        }
        foodRepo.createFoodList()
    }
}