package com.example.foodorderappcompose.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodorderappcompose.data.Food

class FoodRepo {

    private var _foodListRepo = MutableLiveData<List<Food>>()
    val foodListRepo: LiveData<List<Food>>
        get() = _foodListRepo

    fun createFoodList() {
        val fl =  mutableStateListOf<Food>()

        val f1 = Food(1, "Köfte", "kofte", 150.75f)
        val f2 = Food(2, "Pizza", "pizza", 250.00f)
        val f3 = Food(3, "Pide", "pide", 200.95f)
        val f4 = Food(4, "Lahmacun", "lahmacun", 120.25f)
        val f5 = Food(5, "Hamburger", "hamburger", 300.45f)
        val f6 = Food(6, "Sarma", "sarma", 180.00f)
        val f7 = Food(7, "Mantı", "manti", 220.65f)
        val f8 = Food(8, "Çorba", "corba", 90.15f)
        val f9 = Food(10, "Salata", "salata", 110.50f)

        fl.addAll(listOf(f1, f2, f3, f4, f5, f6, f7, f8, f9))

        _foodListRepo.value = fl.toList()
    }
}