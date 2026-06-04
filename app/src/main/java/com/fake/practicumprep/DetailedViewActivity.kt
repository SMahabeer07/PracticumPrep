package com.fake.practicumprep

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailedViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detailed_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtStats = findViewById<TextView>(R.id.txtStatisticsMetrics)
        val txtLog = findViewById<TextView>(R.id.txtDetailedLogData)
        val btnBack = findViewById<Button>(R.id.btnBackToMain)

        val dates = intent.getStringArrayExtra("KEY_DATES") ?: emptyArray()
        val morningTimes = intent.getIntArrayExtra("KEY_MORNING") ?: IntArray(0)
        val afternoonTimes = intent.getIntArrayExtra("KEY_AFTERNOON") ?: IntArray(0)
        val notes = intent.getStringArrayExtra("KEY_NOTES") ?: emptyArray()

        val weeklyRecordList = ArrayList<DailyScreenTime>()
        for (i in dates.indices) {
            weeklyRecordList.add(
                DailyScreenTime(dates[i], morningTimes[i], afternoonTimes[i], notes[i])
            )
        }
        calculateAndDisplayMetrics(weeklyRecordList, txtStats)
        renderDetailedLogDisplay(weeklyRecordList, txtLog)

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun calculateAndDisplayMetrics(list: List<DailyScreenTime>, outputView: TextView) {
        if (list.isEmpty()) return

        var totalSum = 0
        var maxDay = list[0]
        var minDay = list[0]

        for (day in list) {
            val totalDailyMinutes = day.getTotalMinutes()
            totalSum += totalDailyMinutes

            if (totalDailyMinutes > maxDay.getTotalMinutes()) {
                maxDay = day
            }
            if (totalDailyMinutes < minDay.getTotalMinutes()) {
                minDay = day
            }
        }

        val weeklyAverage = totalSum.toDouble() / list.size

        val statisticsSummaryText = """
            • Total Screen Time: $totalSum mins
            • Weekly Average: ${String.format("%.2f", weeklyAverage)} mins/day
            • Highest Usage Day: ${maxDay.date} (${maxDay.getTotalMinutes()} mins)
            • Lowest Usage Day: ${minDay.date} (${minDay.getTotalMinutes()} mins)
        """.trimIndent()

        outputView.text = statisticsSummaryText
    }

    private fun renderDetailedLogDisplay(list: List<DailyScreenTime>, outputView: TextView) {
        val stringBuilder = StringBuilder()

        for (day in list) {
            stringBuilder.append("Date: ${day.date}\n")
            stringBuilder.append(" Morn: ${day.morningMinutes}m | Afternoon: ${day.afternoonMinutes}m\n")
            stringBuilder.append(" Total Daily Track: ${day.getTotalMinutes()} minutes\n")
            stringBuilder.append(" Notes: \"${day.activityNote}\"\n")
            stringBuilder.append("--------------------------------------------------\n\n")
        }

        outputView.text = stringBuilder.toString()
    }
}