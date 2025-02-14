package com.example.foodorderappcompose.datastore

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class AppDatastore(private var context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("foodClickRate")

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: AppDatastore? = null

        fun getInstance(context: Context): AppDatastore {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppDatastore(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    suspend fun writeOrderCount(foodId: Int, value: Int) {
        context.dataStore.edit { preferences ->
            preferences[intPreferencesKey("food_$foodId")] = value
        }
    }

    suspend fun readOrderCount(foodId: Int): Int {
        val preferences = context.dataStore.data.first()
        return preferences[intPreferencesKey("food_$foodId")] ?: 0
    }


    suspend fun deleteOrderCount(foodId: Int) {
        context.dataStore.edit {
            it.remove(intPreferencesKey("food_$foodId"))
        }
    }
}