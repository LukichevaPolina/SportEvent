package com.sport.event.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.R

class RegistrationScreen2 : AppCompatActivity() {

    lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration2)
        buttonNext = findViewById(R.id.btnNext)
        buttonNext.setOnClickListener {
            startThirdRegistartionActivity()
        }
    }

    fun startThirdRegistartionActivity() {
        val country = (findViewById<View>(R.id.country) as TextView).text.toString()
        val locality = (findViewById<View>(R.id.locality) as TextView).text.toString()

        val newIntent = Intent(this, RegistrationScreen3 ::class.java)
        val bundle = intent.extras
        if (bundle != null) {
            bundle.putString("country", country)
            bundle.putString("locality", locality)
            newIntent.putExtras(bundle)
        }
        startActivity(newIntent)
    }
}