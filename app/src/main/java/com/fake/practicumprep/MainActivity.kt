package com.fake.practicumprep

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnEnter = findViewById<Button>(R.id.btnEnter)
        val btnExit = findViewById<Button>(R.id.btnExit)

        btnEnter.setOnClickListener {
            val intent = Intent(this, Main_Screen::class.java)
            startActivity(intent)
        }

        //Remember to use finishAffinity when closing the app
        btnExit.setOnClickListener {
            finishAffinity()
        }
    }
}