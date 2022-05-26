package com.sport.event.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.Constants
import com.sport.event.R

class RegistrationScreen1 : AppCompatActivity() {

    lateinit var buttonNext: Button
    private lateinit var buttonBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration1)
        buttonNext = findViewById(R.id.btnNext)
        buttonNext.setOnClickListener {
            startSecondRegistartionActivity()
        }
        buttonBack = findViewById(R.id.icon_back)
        buttonBack.setOnClickListener {
            val intent = Intent(this, StartScreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun startSecondRegistartionActivity() {
        val name = (findViewById<View>(R.id.name) as TextView).text.toString()
        val surname = (findViewById<View>(R.id.surname) as TextView).text.toString()
        val birthday = (findViewById<View>(R.id.birthday) as TextView).text.toString()
        val email = (findViewById<View>(R.id.email) as TextView).text.toString()

        val data = Bundle()
        data.putString(Constants.NAME, name)
        data.putString(Constants.SURNAME, surname)
        data.putString(Constants.BIRTHDAY, birthday)
        data.putString(Constants.EMAIL, email)

        val intent = Intent(this, RegistrationScreen2::class.java)
        intent.putExtras(data)
        startActivity(intent)

    }
}
