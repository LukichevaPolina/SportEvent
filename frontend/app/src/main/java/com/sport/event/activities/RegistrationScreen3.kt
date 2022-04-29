package com.sport.event.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.R

class RegistrationScreen3 : AppCompatActivity() {
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration3)
        button = findViewById(R.id.btnRegistry)
        button.setOnClickListener() {
            registration()
        }
    }

    fun registration(){
        println(intent.extras)
    }
}