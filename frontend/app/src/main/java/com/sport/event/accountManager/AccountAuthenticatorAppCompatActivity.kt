package com.sport.event.accountManager
import android.accounts.AccountManager
import android.content.Intent.getIntent
import android.os.Bundle
import android.accounts.AccountAuthenticatorResponse
import androidx.appcompat.app.AppCompatActivity

//This class needs for Authenticator activity,
//because AppCompatActivity() does not contain fun setAccountAuthenticatorResult
open class AccountAuthenticatorAppCompatActivity : AppCompatActivity() {
    private var mAccountAuthenticatorResponse: AccountAuthenticatorResponse? = null
    private var mResultBundle: Bundle? = null
    fun setAccountAuthenticatorResult(result: Bundle?) {
        mResultBundle = result
    }

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        mAccountAuthenticatorResponse =
            intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)
        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse!!.onRequestContinued()
        }
    }

    override fun finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse!!.onResult(mResultBundle)
            } else {
                mAccountAuthenticatorResponse!!.onError(
                    AccountManager.ERROR_CODE_CANCELED,
                    "canceled"
                )
            }
            mAccountAuthenticatorResponse = null
        }
        super.finish()
    }
}