package com.sport.event.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.R
import com.sport.event.accountManager.AuthenticatorActivity

class RegistrationScreenReady : AppCompatActivity() {

    private lateinit var buttonBack: ImageButton
    private lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_ready)
        buttonNext = findViewById(R.id.btnNext)
        buttonNext.setOnClickListener {
            val intent = Intent(this, AuthenticatorActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonBack = findViewById(R.id.icon_back)
        buttonBack.setOnClickListener {
            val intent = Intent(this, RegistrationScreen3::class.java)
            startActivity(intent)
            finish()
        }
    }
}