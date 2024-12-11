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

                //children: all objects under the reference point (the reference point in this case is 'foods'), child: each object in 'children'
                for(child in snapshot.children) {
                    val food = child.getValue(Food::class.java)
                    food?.let { foodList.add(it) }
                }

                _foodListRepo.value = foodList
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}