package com.sport.event.activities

import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.Constants
import com.sport.event.R

class RegistrationScreenReady : AppCompatActivity() {

    private lateinit var buttonBack: ImageButton
    private lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_ready)
        buttonNext = findViewById(R.id.btnEnter)
        buttonNext.setOnClickListener {
            authenticate()
        }
        buttonBack = findViewById(R.id.icon_back)
        buttonBack.setOnClickListener {
            val intent = Intent(this, RegistrationScreen3::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun authenticate() {
        val accountManager: AccountManager = AccountManager.get(this@RegistrationScreenReady)
        //accountManager.addAccount opens AuthenticationActivity which creates account on device
        //future needs for debug
        val future : AccountManagerFuture<Bundle> = accountManager.addAccount(
            Constants.ACCOUNT_TYPE, Constants.AUTH_TOKEN_TYPE,null,  null, this,
            { future ->
                try {
                    val bnd: Bundle  = future.getResult()
                    Log.d("SportEvent", "AddNewAccount Bundle is " + bnd)
                    startMainActivity()
                } catch (e: Exception) {
                    e.printStackTrace();
                    println(e)
                }
            }, null)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}