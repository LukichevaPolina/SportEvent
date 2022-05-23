package com.sport.event.accountManager

import com.sport.event.Constants
import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.sport.event.retrofit.APIApp
import java.lang.Exception
import com.sport.event.R
import com.sport.event.activities.StartScreen
import com.sport.event.retrofit.models.LoginRequest
import com.sport.event.retrofit.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//The Authenticator activity.
//Called by the Authenticator and in charge of identifing the user.
//It sends back to the Authenticator the result.
class AuthenticatorActivity : AccountAuthenticatorAppCompatActivity() {
    private val REQ_SIGNUP = 1
    private val TAG = this.javaClass.simpleName
    private var mAccountManager: AccountManager? = null
    private var mAuthTokenType: String? = null
    private lateinit var buttonBack: ImageButton

    //Called when the activity is first created.
    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_login)
        findViewById<View>(R.id.loadingPanel).visibility = View.GONE
        mAccountManager = AccountManager.get(baseContext)
        val accountName = intent.getStringExtra(Constants.ACCOUNT_NAME)
        mAuthTokenType = intent.getStringExtra(Constants.AUTH_TOKEN_TYPE)
        if (accountName != null) {
            (findViewById<View>(R.id.email) as TextView).text = accountName
        }
        findViewById<View>(R.id.btnLogin).setOnClickListener {
            submit()
        }
        buttonBack = findViewById(R.id.icon_back)
        buttonBack.setOnClickListener {
            val intent = Intent(this, StartScreen::class.java)
            startActivity(intent)
            finish()
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

    fun submit() {
        findViewById<View>(R.id.loadingPanel).visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        val userEmail = (findViewById<View>(R.id.email) as TextView).text.toString()
        val userPass =
            (findViewById<View>(R.id.password) as TextView).text.toString()
        val accountType = intent.getStringExtra(Constants.ACCOUNT_TYPE)

        val data = Bundle()
        val userdata = Bundle()
        try {
            val userLogin = LoginRequest(userEmail, userPass)
            //-----------------------------------Retrofit request-----------------------------------
            APIApp.restClient?.service?.loginUser(userLogin)?.enqueue(object :
                Callback<LoginResponse?> {
                override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
                    val loginResponse: LoginResponse? = response.body()
                    if (response.isSuccessful && loginResponse != null) {
                        userdata.putString(Constants.REFRESH_TOKEN, loginResponse.getTokens()?.getRefreshToken())
                        userdata.putString(Constants.USER_ID, loginResponse.getId().toString())
                        userdata.putString(Constants.USERNAME, loginResponse.getUsername())

                        data.putString(AccountManager.KEY_AUTHTOKEN, loginResponse.getTokens()?.getAccessToken())
                        data.putString(AccountManager.KEY_ACCOUNT_NAME, userEmail)
                        data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType)
                        data.putString(PARAM_USER_PASS, userPass)
                        data.putBundle(Constants.USER_DATA, userdata)

                        val res = Intent()
                        res.putExtras(data)
                        finishLogin(res)
                    } else {
                        when (response.code()) {
                            400, 401 -> Toast.makeText(applicationContext, "Incorrect login or password, try again", Toast.LENGTH_LONG).show()
                            else -> {
                                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
                            }
                        }
                        findViewById<View>(R.id.loadingPanel).visibility = View.GONE
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }
                    }
                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    Toast.makeText(applicationContext, "Connection to server failed, try again later", Toast.LENGTH_LONG).show()
                        findViewById<View>(R.id.loadingPanel).visibility = View.GONE
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            })
        //--------------------------------------------------------------------------------------
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
        mAccountManager!!.addAccountExplicitly(account, accountPassword, intent.getBundleExtra(
            Constants.USER_DATA))
        mAccountManager!!.setAuthToken(account, authtokenType, authtoken)
        setAccountAuthenticatorResult(intent.extras)
        setResult(RESULT_OK, intent)
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        finish()
    }

    companion object {
        const val KEY_ERROR_MESSAGE = "ERR_MSG"
        const val PARAM_USER_PASS = "USER_PASS"
    }
}