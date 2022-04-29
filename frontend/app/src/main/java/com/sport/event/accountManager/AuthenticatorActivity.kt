package com.sport.event.accountManager

import AccountUtils
import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sport.event.retrofit.RestClientCallbacks
import com.sport.event.retrofit.APIApp
import java.lang.Exception
import com.sport.event.R




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
        setContentView(R.layout.activity_login)
        mAccountManager = AccountManager.get(baseContext)
        val accountName = intent.getStringExtra(AccountUtils.ACCOUNT_NAME)
        mAuthTokenType = intent.getStringExtra(AccountUtils.ARG_AUTH_TOKEN_TYPE)
        if (accountName != null) {
            (findViewById<View>(R.id.email) as TextView).text = accountName
        }
        findViewById<View>(R.id.btnLogin).setOnClickListener {
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
        val userEmail = (findViewById<View>(R.id.email) as TextView).text.toString()
        val userPass =
            (findViewById<View>(R.id.password) as TextView).text.toString()
        val accountType = intent.getStringExtra(AccountUtils.ACCOUNT_TYPE)

        Log.d("SportEvent", "$TAG> Started authenticating")
        var authToken: String? = null
        val data = Bundle()
        try {
            //-----------------------------------Retrofit request-----------------------------------
            APIApp.restClient?.login(userEmail, userPass, object : RestClientCallbacks {
                override fun onSuccess(authToken: String?) {
                    println(authToken)
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, userEmail)
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType)
                    data.putString(AccountManager.KEY_AUTHTOKEN, authToken)
                    data.putString(PARAM_USER_PASS, userPass);
                    val res = Intent()
                    res.putExtras(data)
                    finishLogin(res)
                }

                override fun onFailure(code: Int?) {
                    when (code) {
                        400, 401 -> Toast.makeText(applicationContext, "Incorrect login or password, try again", Toast.LENGTH_LONG).show()
                        else -> {
                            Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                override fun onError(throwable: Throwable?) {
                    Toast.makeText(applicationContext, "Connection to server failed, try again later", Toast.LENGTH_LONG).show()
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
        val authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN)
        val authtokenType = mAuthTokenType

        // Creating the account on the device and setting the auth token we got
        // (Not setting the auth token will cause another call to the server to authenticate the user)
        mAccountManager!!.addAccountExplicitly(account, accountPassword, null)
        mAccountManager!!.setAuthToken(account, authtokenType, authtoken)
        setAccountAuthenticatorResult(intent.extras)
        setResult(RESULT_OK, intent)
        finish()
    }

        companion object {
        const val KEY_ERROR_MESSAGE = "ERR_MSG"
        const val PARAM_USER_PASS = "USER_PASS"
    }
}