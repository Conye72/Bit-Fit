package com.example.bitfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FoodListFragment : Fragment() {

    private lateinit var foodEntryDao: FoodDao
    private lateinit var foodEntryAdapter: FoodEntryAdapter
    private val foodEntries = mutableListOf<FoodEntry>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodEntryDao = (requireActivity().application as FoodApplication).db.foodDao()

        val recyclerView = view.findViewById<RecyclerView>(R.id.food_entries_recycler_view)
        foodEntryAdapter = FoodEntryAdapter(foodEntries)
        recyclerView.adapter = foodEntryAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            foodEntryDao.getAll().collect {
                foodEntries.clear()
                foodEntries.addAll(it)
                foodEntryAdapter.notifyDataSetChanged()
            }
        }
    }
}