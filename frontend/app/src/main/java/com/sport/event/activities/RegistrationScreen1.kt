package com.sport.event.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.R

class RegistrationScreen1 : AppCompatActivity() {

    lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration1)
        buttonNext = findViewById(R.id.btnNext)
        buttonNext.setOnClickListener {
            startSecondRegistartionActivity()
        }
    }

    fun startSecondRegistartionActivity() {
        val name = (findViewById<View>(R.id.name) as TextView).text.toString()
        val surname = (findViewById<View>(R.id.surname) as TextView).text.toString()
        val birthday = (findViewById<View>(R.id.birthday) as TextView).text.toString()
        val email = (findViewById<View>(R.id.email) as TextView).text.toString()

        val data = Bundle()
        data.putString("name", name)
        data.putString("surname", surname)
        data.putString("dateOfBirth", birthday)
        data.putString("email", email)

        val intent = Intent(this, RegistrationScreen2::class.java)
        intent.putExtras(data)
        startActivity(intent)

    }
}