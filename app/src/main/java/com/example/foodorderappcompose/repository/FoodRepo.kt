package com.example.foodorderappcompose.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodorderappcompose.data.Food
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FoodRepo {

    private var referenceFoods: DatabaseReference = FirebaseDatabase.getInstance().getReference("foods")

    private var _foodListRepo = MutableLiveData<List<Food>>()
    val foodListRepo: LiveData<List<Food>>
        get() = _foodListRepo

    fun createFoodList() {
        referenceFoods.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val foodList =  ArrayList<Food>()

                snapshot.children.forEach { dataSnapshot ->
                    val food = dataSnapshot.getValue(Food::class.java)
                    food?.let { _food ->
                        Log.d("TAG", dataSnapshot.key.toString())
                        food.id = _food.id
                        foodList.add(food)
                    }
                }

                _foodListRepo.value = foodList
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}