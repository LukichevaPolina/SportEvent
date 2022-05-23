package com.sport.event.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.accounts.AccountManager
import android.accounts.Account
import com.sport.event.Constants
import android.annotation.SuppressLint
import com.sport.event.R

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    var mAccountManager: AccountManager? = null

    val authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SportEvent)
        super.onCreate(savedInstanceState)
//        val intent = Intent(this, Onbording1::class.java)
//        startActivity(intent)
        routeToAppropriatePage()
        finish()
    }

    private fun routeToAppropriatePage() {
        val account: Account? = accountAvailable()
        if (account == null) {
            //if there is no account on the device -> Onboardings
            val intent = Intent(this, Onbording1::class.java)
            startActivity(intent)
//              finish()
        } else {
            //if there is account on the device -> MapsActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
//              finish()
        }
    }

    private fun accountAvailable():Account? {
        //get accountManager
        val accountManager: AccountManager = AccountManager.get(this@SplashScreen)
        //get array of Accounts from Account manager
        val accounts: Array<Account> = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE)
        for (acc in accounts) {
            //looking for the right type of account
            if (acc.type.equals(Constants.ACCOUNT_TYPE, ignoreCase = true)) {
                return acc
            }
        }
        return null
    }
}