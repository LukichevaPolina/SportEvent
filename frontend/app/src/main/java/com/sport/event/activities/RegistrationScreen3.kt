package com.sport.event.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.R
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.RestClientCallbacks

class RegistrationScreen3 : AppCompatActivity() {
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration3)
        findViewById<View>(R.id.loadingPanel).visibility = View.GONE
        button = findViewById(R.id.btnRegistry)
        button.setOnClickListener() {
            registration()
        }
    }

    fun registration(){
        findViewById<View>(R.id.loadingPanel).visibility = View.VISIBLE

        //get user data for registration
        val name = intent.getStringExtra(Constants.NAME)
        val surname = intent.getStringExtra(Constants.SURNAME)
        val birthday = intent.getStringExtra(Constants.BIRTHDAY)
        val email = intent.getStringExtra(Constants.EMAIL)
        val country = intent.getStringExtra(Constants.COUNTRY)
        val locality = intent.getStringExtra(Constants.LOCALITY)
        val username = (findViewById<View>(R.id.username) as TextView).text.toString()
        val favoriteSports = (findViewById<View>(R.id.favorite_sports) as TextView).text.toString()
        val password = (findViewById<View>(R.id.password) as TextView).text.toString()
        val repeatPassword = (findViewById<View>(R.id.repeat_password) as TextView).text.toString()

        val arrayFavoriteSports = arrayListOf<Int>(favoriteSports.toInt())

        //check passwords matching
        if (password != repeatPassword) {
            Toast.makeText(applicationContext, "Passwords don't match!", Toast.LENGTH_LONG).show()
            return
        }

        //send request to server for register user
        APIApp.restClient?.register(name, surname, birthday, email, country, locality, username, arrayFavoriteSports, password, object : RestClientCallbacks {
            override fun onSuccess(value: String?) {
                startCheckEmailActivity()
            }

            override fun onFailure(code: Int?) {
                when (code) {
                    400, 401 -> Toast.makeText(applicationContext, "Incorrect data", Toast.LENGTH_LONG).show()
                    else -> {
                        Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onError(throwable: Throwable?) {
                Toast.makeText(applicationContext, "Connection to server failed, try again later", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun startCheckEmailActivity() {
        val intent = Intent(this, CheckEmailScreen::class.java)
        startActivity(intent)
        finish()
    }

}