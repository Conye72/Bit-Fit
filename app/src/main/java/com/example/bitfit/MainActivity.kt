package com.example.bitfit

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var foodEntryDao: FoodDao
    private lateinit var foodEntryAdapter: FoodEntryAdapter
    private val foodEntries = mutableListOf<FoodEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        foodEntryDao = (application as FoodApplication).db.foodDao()

        val recyclerView = findViewById<RecyclerView>(R.id.food_entries_recycler_view)
        foodEntryAdapter = FoodEntryAdapter(foodEntries)
        recyclerView.adapter = foodEntryAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            foodEntryDao.getAll().collect {
                foodEntries.clear()
                foodEntries.addAll(it)
                foodEntryAdapter.notifyDataSetChanged()
            }
        }

        findViewById<FloatingActionButton>(R.id.add_entry_fab).setOnClickListener {
            showAddFoodDialog()
        }
    }

    private fun showAddFoodDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_food, null)
        val foodNameEditText = dialogView.findViewById<EditText>(R.id.food_name_edit_text)
        val caloriesEditText = dialogView.findViewById<EditText>(R.id.calories_edit_text)

        AlertDialog.Builder(this)
            .setTitle("Add Food Entry")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val foodName = foodNameEditText.text.toString()
                val calories = caloriesEditText.text.toString().toIntOrNull() ?: 0
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                lifecycleScope.launch {
                    foodEntryDao.insert(FoodEntry(date = currentDate, foodName = foodName, calories = calories))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}