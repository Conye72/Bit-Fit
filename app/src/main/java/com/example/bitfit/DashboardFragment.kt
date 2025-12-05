package com.example.bitfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private lateinit var foodEntryDao: FoodDao
    private lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodEntryDao = (requireActivity().application as FoodApplication).db.foodDao()
        lineChart = view.findViewById(R.id.line_chart)

        lifecycleScope.launch {
            foodEntryDao.getAll().collect { entries ->
                val chartEntries = entries.mapIndexed { index, entry ->
                    Entry(index.toFloat(), entry.calories.toFloat())
                }
                val lineDataSet = LineDataSet(chartEntries, "Calories")
                val lineData = LineData(lineDataSet)
                lineChart.data = lineData
                lineChart.invalidate()
            }
        }
    }
}
