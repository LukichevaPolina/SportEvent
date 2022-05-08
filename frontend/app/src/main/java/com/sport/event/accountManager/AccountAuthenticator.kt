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
    // TODO: getAuthToken
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
//        var authToken = am.peekAuthToken(account, authTokenType)
//
//        // Lets give another try to authenticate the user
//        if (TextUtils.isEmpty(authToken)) {
//            val password = am.getPassword(account)
//            if (password != null) {
//                try {
//                    authToken =
//                        sServerAuthenticate.userSignIn(account.name, password, authTokenType)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//
//        // If we get an authToken - we return it
//        if (!TextUtils.isEmpty(authToken)) {
//            val result = Bundle()
//            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
//            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
//            result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
//            return result
//        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        val intent = Intent(mContext, AuthenticatorActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(Constants.ACCOUNT_TYPE, account.type)
        intent.putExtra(Constants.ACCOUNT_TYPE, authTokenType)
        intent.putExtra(Constants.ACCOUNT_NAME, account.name)
        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
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
        result.putBoolean(KEY_BOOLEAN_RESULT, false)
        return result
    }

}
