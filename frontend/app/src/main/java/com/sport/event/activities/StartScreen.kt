package com.sport.event.activities

import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.Constants
import com.sport.event.R
import com.sport.event.activities.authActivities.RegistrationScreen1

class StartScreen : AppCompatActivity() {

    private lateinit var buttonLogin: Button
    private lateinit var buttonRegistry: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)
        buttonLogin = findViewById(R.id.btnLogin)
        buttonLogin.setOnClickListener() {
             authenticate()
        }
        buttonRegistry = findViewById(R.id.btnRegistry)
        buttonRegistry.setOnClickListener() {
            val intent = Intent(this, RegistrationScreen1::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun authenticate() {
        val accountManager: AccountManager = AccountManager.get(this@StartScreen)
        //accountManager.addAccount opens AuthenticationActivity which creates account on device
        //future needs for debug
        val future : AccountManagerFuture<Bundle> = accountManager.addAccount(
            Constants.ACCOUNT_TYPE, Constants.AUTH_TOKEN_TYPE,null,  null, this,
            { future ->
                try {
                    val bnd: Bundle  = future.getResult()
                    Log.d("SportEvent", "AddNewAccount Bundle is " + bnd)
                    startMapActivity()
                } catch (e: Exception) {
                    e.printStackTrace();
                    println(e)
                }
            }, null)
    }


    private fun startMapActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
