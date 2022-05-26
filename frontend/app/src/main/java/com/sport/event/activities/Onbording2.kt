package com.sport.event.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sport.event.R

class Onbording2 : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onbording2)
        button = findViewById(R.id.btnNext)
        button.setOnClickListener() {
            val intent = Intent(this, Onbording3::class.java)
            startActivity(intent)
            finish()
        }
    }
}
