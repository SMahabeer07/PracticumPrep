package com.fake.practicumprep

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Main_Screen : AppCompatActivity() {

    private val standardDates = arrayOf(
        "2024-04-02", "2024-04-03", "2024-04-04",
        "2024-04-05", "2024-04-06", "2024-04-07", "2024-04-08"
    )

    // Parallel arrays required for data tracking
    private val morningTimes = IntArray(7)
    private val afternoonTimes = IntArray(7)
    private val activityNotes = Array(7) { "" }

    private var currentDayIndex = 0
    private lateinit var txtDayIndicator: TextView
    private lateinit var txtDateIndicator: TextView
    private lateinit var edtMorning: EditText
    private lateinit var edtAfternoon: EditText
    private lateinit var edtNotes: EditText
    private lateinit var btnSave: Button
    private lateinit var btnClear: Button
    private lateinit var btnDetailedView: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        txtDayIndicator = findViewById(R.id.txtCurrentDayIndicator)
        txtDateIndicator = findViewById(R.id.txtTargetDate)
        edtMorning = findViewById(R.id.edtMorningTime)
        edtAfternoon = findViewById(R.id.edtAfternoonTime)
        edtNotes = findViewById(R.id.edtNotes)
        btnSave = findViewById(R.id.btnSaveDay)
        btnClear = findViewById(R.id.btnClearData)
        btnDetailedView = findViewById(R.id.btnGoToDetailed)

        updateUiForCurrentDay()

        btnSave.setOnClickListener {
            handleDataInversion()
        }

        btnClear.setOnClickListener {
            clearAllInputs()
        }

        btnDetailedView.setOnClickListener {
            val intent = Intent(this, DetailedViewActivity::class.java).apply {
                putExtra("KEY_DATES", standardDates)
                putExtra("KEY_MORNING", morningTimes)
                putExtra("KEY_AFTERNOON", afternoonTimes)
                putExtra("KEY_NOTES", activityNotes)
            }
            startActivity(intent)
        }
    }

    private fun handleDataInversion() {
        val morningInput = edtMorning.text.toString().trim()
        val afternoonInput = edtAfternoon.text.toString().trim()
        val noteInput = edtNotes.text.toString().trim()

        if (morningInput.isEmpty() || afternoonInput.isEmpty() || noteInput.isEmpty()) {
            Toast.makeText(this, "Error: Please fill in all fields before saving.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val morningMins = morningInput.toInt()
            val afternoonMins = afternoonInput.toInt()

            if (morningMins < 0 || afternoonMins < 0) {
                Toast.makeText(this, "Error: Screen time values cannot be negative.", Toast.LENGTH_SHORT).show()
                return
            }

            // Storing structural tracking inside the assigned Parallel Arrays
            morningTimes[currentDayIndex] = morningMins
            afternoonTimes[currentDayIndex] = afternoonMins
            activityNotes[currentDayIndex] = noteInput

            currentDayIndex++

            if (currentDayIndex >= 7) {
                btnSave.isEnabled = false
                btnDetailedView.isEnabled = true
                Toast.makeText(this, "All 7 days complete! View full summary details.", Toast.LENGTH_LONG).show()
            } else {
                updateUiForCurrentDay()
                clearInputFormFields()
            }

        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Error: Invalid numeric formatting input detected.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUiForCurrentDay() {
        if (currentDayIndex < 7) {
            txtDayIndicator.text = "Entering Data for Day ${currentDayIndex + 1} of 7"
            txtDateIndicator.text = "Date: ${standardDates[currentDayIndex]}"
        }
    }

    private fun clearInputFormFields() {
        edtMorning.text.clear()
        edtAfternoon.text.clear()
        edtNotes.text.clear()
    }

    private fun clearAllInputs() {
        currentDayIndex = 0
        for (i in 0..6) {
            morningTimes[i] = 0
            afternoonTimes[i] = 0
            activityNotes[i] = ""
        }
        clearInputFormFields()
        updateUiForCurrentDay()
        btnSave.isEnabled = true
        btnDetailedView.isEnabled = false
        Toast.makeText(this, "Application data cache completely reset.", Toast.LENGTH_SHORT).show()
    }
}