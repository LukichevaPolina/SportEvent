package com.sport.event.accountManager

import Constants
import android.accounts.NetworkErrorException
import android.os.Bundle
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager.KEY_BOOLEAN_RESULT
import android.accounts.AccountManager
import android.content.Intent
import android.accounts.AbstractAccountAuthenticator
import android.content.Context
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.RestClientCallbacks

class AccountAuthenticator(private val mContext: Context) : AbstractAccountAuthenticator(mContext) {

    //  Returns a Bundle that contains the Intent of the activity that can be used to edit the properties.
    override fun editProperties(
        response: AccountAuthenticatorResponse,
        accountType: String
    ): Bundle? {
        return null
    }

    //  Adds an account of the specified accountType.
    @Throws(NetworkErrorException::class)
    override fun addAccount(
        response: AccountAuthenticatorResponse,
        accountType: String,
        authTokenType: String,
        requiredFeatures: Array<String>?,
        options: Bundle
    ): Bundle {
        val intent = Intent(mContext, AuthenticatorActivity::class.java)
        intent.putExtra(Constants.ACCOUNT_TYPE, accountType)
        intent.putExtra(Constants.AUTH_TOKEN_TYPE, authTokenType)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    //  Checks that the user knows the credentials of an account.
    @Throws(NetworkErrorException::class)
    override fun confirmCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        options: Bundle
    ): Bundle? {
        return null
    }

    //  Gets an authtoken for an account.
    // It will call after calling invalidateAuthToken upon receiving a 401 or 403
    @Throws(NetworkErrorException::class)
    override fun getAuthToken(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle
    ): Bundle {

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        val am = AccountManager.get(mContext)
        var authToken = am.peekAuthToken(account, authTokenType)
        println("Ajndbcfjvbhskdhfv!!!!!!!!!!!!!!!!!")
//        checkValidToken(authToken, am, account)

        return Bundle()
    }

//    private fun checkValidToken(token: String, am: AccountManager, account: Account) {
//        APIApp.restClient?.verifyToken(token,  object : RestClientCallbacks {
//            override fun onSuccess(value: Bundle?) {
//                val authToken = value?.getString(Constants.VERIFY_TOKEN)
//                print("AUTHHH TOKEEEEEEEEEEEEEN" + authToken)
//            }
//
//            override fun onFailure(code: Int?) {
//                when (code) {
//                    400, 401 -> println("Incorrect data")
//                    else -> println("Something went wrong")
//                }
//            }
//            override fun onError(throwable: Throwable?) {
//                println("Connection to server failed, try again later")
//            }
//        })
//    }

    //  Ask the authenticator for a localized label for the given authTokenType.
    override fun getAuthTokenLabel(authTokenType: String): String {
        return "$authTokenType (Label)"
    }

    //  Update the locally stored credentials for an account.
    @Throws(NetworkErrorException::class)
    override fun updateCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle
    ): Bundle? {
//        val accountManager: AccountManager = get(mContext)
//        val refreshToken = accountManager.getUserData(account,Constants.REFRESH_TOKEN)
//        try {
//            APIApp.restClient?.refreshToken(refreshToken, object : RestClientCallbacks {
//                override fun onSuccess(value: Bundle?) {
//                    val authToken = value?.getString(Constants.AUTH_TOKEN)
//                    accountManager.setAuthToken(account, Constants.AUTH_TOKEN_TYPE, authToken)
//                }
//
//                override fun onFailure(code: Int?) {
//                    when (code) {
//                        400, 401 -> println("Incorrect data")
//                        else -> println("Something went wrong")
//                    }
//                }
//                override fun onError(throwable: Throwable?) {
//                    println("Connection to server failed, try again later")
//                }
//            })
//        } catch (e: Exception) {
//            println(e.message)
//        }
        return null
    }

    //  Checks if the account supports all the specified authenticator specific features.
    @Throws(NetworkErrorException::class)
    override fun hasFeatures(
        response: AccountAuthenticatorResponse,
        account: Account,
        features: Array<String>
    ): Bundle {
        val result = Bundle()
        result.putBoolean(KEY_BOOLEAN_RESULT, false)
        return result
    }

}
