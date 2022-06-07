package com.sport.event.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.R

class NoNetworkConnection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val imageView : ImageView= findViewById(R.id.imageViewOnb1)
        //imageView.setImageResource(R.drawable.ic_onb1backgr)
        setContentView(R.layout.activity_no_network_connection)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener{
            val intent = Intent(this, SplashScreen::class.java)
            startActivity(intent)
        }
    }
}
