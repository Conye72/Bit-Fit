package com.example.bitfit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        findViewById<FloatingActionButton>(R.id.add_entry_fab).setOnClickListener {
            showAddFoodDialog()
        }

        scheduleDailyNotification()
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
                    val foodEntryDao = (application as FoodApplication).db.foodDao()
                    foodEntryDao.insert(FoodEntry(date = currentDate, foodName = foodName, calories = calories))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun scheduleDailyNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 20) // 8 PM
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}