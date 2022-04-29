package com.sport.event.activities

import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.R
import com.sport.event.accountManager.AuthenticatorActivity
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.RestClientCallbacks
import org.w3c.dom.Text

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
        val name = intent.getStringExtra("name")
        val surname = intent.getStringExtra("surname")
        val birthday = intent.getStringExtra("birthday")
        val email = intent.getStringExtra("email")
        val country = intent.getStringExtra("country")
        val locality = intent.getStringExtra("locality")
        val username = (findViewById<View>(R.id.username) as TextView).text.toString()
        val favoriteSports = (findViewById<View>(R.id.favorite_sports) as TextView).text.toString()
        val password = (findViewById<View>(R.id.password) as TextView).text.toString()
        val repeatPassword = (findViewById<View>(R.id.repeat_password) as TextView).text.toString()


        val arrayFavoriteSports = arrayListOf<Int>(favoriteSports.toInt())
        println(arrayFavoriteSports)

        if (password != repeatPassword) {
            Toast.makeText(applicationContext, "Passwords don't match!", Toast.LENGTH_LONG).show()
            return
        }

        APIApp.restClient?.register(name, surname, birthday, email, country, locality, username, arrayFavoriteSports, password, object : RestClientCallbacks {
            override fun onSuccess(value: String?) {
                println("OK")
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

}