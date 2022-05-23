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

class SuccessfulRegistrationScreen : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_succsessful_registration)
        button = findViewById(R.id.button)
        button.setOnClickListener {
            authenticate()
        }
    }

    fun authenticate() {
        val accountManager: AccountManager = AccountManager.get(this@SuccessfulRegistrationScreen)
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