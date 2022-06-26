package com.sport.event.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.accounts.AccountManager
import android.annotation.SuppressLint
import com.sport.event.R
import com.sport.event.accountManager.AccountManagerHelper
import com.sport.event.activities.onboardings.Onboarding1
import com.sport.event.activities.onboardings.Onbording1Fragment
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.models.Sport
import retrofit2.Call
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    var mAccountManager: AccountManager? = null

    val authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SportEvent)
        super.onCreate(savedInstanceState)
        routeToAppropriatePage()
        finish()
    }

    private fun routeToAppropriatePage() {
        checkNetworkConnection()
        val account = AccountManagerHelper().getAccount(AccountManager.get(this))
        checkNetworkConnection()
        // launch the onboarding screen if it is the first launch of the app
        if (isFirstLaunch()) {
            val intent = Intent(this, Onboarding1::class.java)
            startActivity(intent)
            finish()
        }
        else {
            if (account == null) {
                //if there is no account on the device -> StartScreen
                val intent = Intent(this, StartScreen::class.java)
                startActivity(intent)
//              finish()
            } else {
                //if there is account on the device -> Maps
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
//              finish()
            }
        }
    }

    private fun checkNetworkConnection() {
        APIApp.restClient?.service?.getSports()?.enqueue(object :
            retrofit2.Callback<ArrayList<Sport>> {
            override fun onResponse(
                call: Call<ArrayList<Sport>>,
                response: Response<ArrayList<Sport>>
            ) {
                return
            }

            override fun onFailure(call: Call<ArrayList<Sport>>, t: Throwable) {
                stratNoNetworkActivity()
            }
        })
    }
    private fun stratNoNetworkActivity() {
        val intent = Intent(this, NoNetworkConnection::class.java)
        startActivity(intent)
    }

    private fun isFirstLaunch() : Boolean {
        return SharedPref.getInstance(applicationContext).isFirstLaunch()
    }
}
