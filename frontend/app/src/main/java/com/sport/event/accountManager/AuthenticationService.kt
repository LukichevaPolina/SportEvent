package com.sport.event.accountManager

import android.accounts.AccountManager
import android.app.Service
import android.content.Intent

import android.os.IBinder
import android.annotation.SuppressLint

//service that allows to add account in settings
class AuthenticatorService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        var binder: IBinder? = null
        if (intent.action == AccountManager.ACTION_AUTHENTICATOR_INTENT) {
            binder = authenticator.getIBinder()
        }
        return binder
    }

    private val authenticator: AccountAuthenticator
        get() {
            if (null == AuthenticatorService.Companion.sAccountAuthenticator) {
                AuthenticatorService.Companion.sAccountAuthenticator = AccountAuthenticator(this)
            }
            return AuthenticatorService.Companion.sAccountAuthenticator!!
        }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sAccountAuthenticator: AccountAuthenticator? = null
    }
}