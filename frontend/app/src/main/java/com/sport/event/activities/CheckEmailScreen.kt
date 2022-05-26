package com.sport.event.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.R

class CheckEmailScreen : AppCompatActivity() {

    private lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_email)
        buttonNext = findViewById(R.id.btnNext)
        buttonNext.setOnClickListener {
            val intent = Intent(this, StartScreen::class.java)
            startActivity(intent)
            finish()
        }
    }
}
