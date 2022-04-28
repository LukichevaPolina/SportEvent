package com.sport.event.accountManager

import AccountUtils
import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.sport.event.retrofit.LoginCallbacks
import com.sport.event.retrofit.APIApp
import java.lang.Exception


//The Authenticator activity.
//Called by the Authenticator and in charge of identifing the user.
//It sends back to the Authenticator the result.
class AuthenticatorActivity : AccountAuthenticatorAppCompatActivity() {
    private val REQ_SIGNUP = 1
    private val TAG = this.javaClass.simpleName
    private var mAccountManager: AccountManager? = null
    private var mAuthTokenType: String? = null

    //Called when the activity is first created.
    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(com.sport.event.R.layout.activity_login)
        mAccountManager = AccountManager.get(baseContext)
        val accountName = intent.getStringExtra(AccountUtils.ACCOUNT_NAME)
        mAuthTokenType = intent.getStringExtra(AccountUtils.ARG_AUTH_TOKEN_TYPE)
        if (accountName != null) {
            (findViewById<View>(com.sport.event.R.id.email) as TextView).text = accountName
        }
        findViewById<View>(com.sport.event.R.id.btnLogin).setOnClickListener {
            submit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            if (data != null) {
                finishLogin(data)
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    // TODO: different cases with invalid data
    fun submit() {
        val userName = (findViewById<View>(com.sport.event.R.id.email) as TextView).text.toString()
        val userPass =
            (findViewById<View>(com.sport.event.R.id.password) as TextView).text.toString()
        val accountType = intent.getStringExtra(AccountUtils.ACCOUNT_TYPE)

        Log.d("SportEvent", "$TAG> Started authenticating")
        var authToken: String? = null
        val data = Bundle()
        try {
            //-----------------------------------Retrofit request-----------------------------------
            APIApp.restClient?.login(userName, userPass, object : LoginCallbacks {
                override fun onSuccess(authToken: String?) {
                    println(authToken)
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, userName)
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType)
                    data.putString(AccountManager.KEY_AUTHTOKEN, authToken)
                    data.putString(PARAM_USER_PASS, userPass);
                    val res = Intent()
                    res.putExtras(data)
                    finishLogin(res)
                }
                override fun onError(throwable: Throwable?) {
                    println("не получилосьб ><")
                }
            })
        } catch (e: Exception) {
            data.putString(KEY_ERROR_MESSAGE, e.message)
        }.toString()
    }


    private fun finishLogin(intent: Intent) {
        val accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
        val accountPassword = intent.getStringExtra(PARAM_USER_PASS)
        val account = Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE))
//        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
        val authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN)
        val authtokenType = mAuthTokenType

        // Creating the account on the device and setting the auth token we got
        // (Not setting the auth token will cause another call to the server to authenticate the user)
        mAccountManager!!.addAccountExplicitly(account, accountPassword, null)
        mAccountManager!!.setAuthToken(account, authtokenType, authtoken)
//        } else {
//            Log.d("udinic", "$TAG> finishLogin > setPassword")
//            mAccountManager!!.setPassword(account, accountPassword)
//        }
        setAccountAuthenticatorResult(intent.extras)
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        const val KEY_ERROR_MESSAGE = "ERR_MSG"
        const val PARAM_USER_PASS = "USER_PASS"
    }
}