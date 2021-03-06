package com.sport.event.accountManager

import com.sport.event.Constants
import android.accounts.NetworkErrorException
import android.os.Bundle
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Intent
import android.accounts.AbstractAccountAuthenticator
import android.content.Context
import android.text.TextUtils
import com.sport.event.activities.authActivities.AuthenticatorActivity
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.models.RefreshTokenRequest
import com.sport.event.retrofit.models.RefreshTokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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
        lateinit var refreshToken: String
        if (TextUtils.isEmpty(authToken)) {
            refreshToken = am.getUserData(account, Constants.REFRESH_TOKEN)
            val refreshTokenRequest: RefreshTokenRequest = RefreshTokenRequest(refreshToken)
            //get authtokin with using coroutine
            val tokens = runBlocking {
                 refresh(refreshTokenRequest)
            } as RefreshTokenResponse
            authToken = tokens.access
            refreshToken = tokens.refresh.toString()
        }
        val result = Bundle()
        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
        result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
        am.setUserData(account, Constants.REFRESH_TOKEN, refreshToken)
        return result
    }

    private suspend fun refresh(refreshTokenRequest: RefreshTokenRequest) = withContext(Dispatchers.IO) {
            APIApp.restClient?.service?.refresh(refreshTokenRequest)
    }

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
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false)
        return result
    }
}
