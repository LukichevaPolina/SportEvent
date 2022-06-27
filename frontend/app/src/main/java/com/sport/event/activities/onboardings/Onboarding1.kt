package com.sport.event.activities.onboardings

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.R
import com.sport.event.activities.StartScreen

class Onboarding1 : AppCompatActivity() {

    private lateinit var buttonNext: Button
    private lateinit var buttonSkip: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val imageView : ImageView= findViewById(R.id.imageViewOnb1)
        //imageView.setImageResource(R.drawable.ic_onb1backgr)
        setContentView(R.layout.activity_onbording1)
        buttonNext = findViewById(R.id.btnNext)
        buttonSkip = findViewById(R.id.btnSkip)
        buttonNext.setOnClickListener {
            val intent = Intent(this, Onboarding2::class.java)
            startActivity(intent)
            finish()
        }
        buttonSkip.setOnClickListener {
            val intent = Intent(this, StartScreen::class.java)
            startActivity(intent)
            finish()
        }
    }
}
