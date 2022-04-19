package com.sport.event

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Onbording1 : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val imageView : ImageView= findViewById(R.id.imageViewOnb1)
        //imageView.setImageResource(R.drawable.ic_onb1backgr)
        setContentView(R.layout.activity_onbording1)
        button = findViewById(R.id.btnNext)
        button.setOnClickListener() {
            val intent = Intent(this, Onbording2::class.java)
            startActivity(intent)
            //finish()
        }
    }
}