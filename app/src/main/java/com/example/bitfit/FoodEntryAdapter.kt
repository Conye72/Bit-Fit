package com.example.bitfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodEntryAdapter(private val entries: List<FoodEntry>) : RecyclerView.Adapter<FoodEntryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodNameTextView: TextView = view.findViewById(R.id.food_name_text_view)
        val caloriesTextView: TextView = view.findViewById(R.id.calories_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_entry_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.foodNameTextView.text = entry.foodName
        holder.caloriesTextView.text = "Calories: ${entry.calories}"
    }

    override fun getItemCount() = entries.size
}