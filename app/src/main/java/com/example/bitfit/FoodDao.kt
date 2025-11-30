package com.example.bitfit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert
    suspend fun insert(foodEntry: FoodEntry)

    @Query("SELECT * FROM food_entries ORDER BY date DESC")
    fun getAll(): Flow<List<FoodEntry>>
}