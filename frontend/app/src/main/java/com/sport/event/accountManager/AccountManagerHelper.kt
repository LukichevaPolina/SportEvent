package com.sport.event.accountManager

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.os.Bundle
import com.sport.event.Constants

class AccountManagerHelper {
    fun getFutureUpdateToken(accountManager: AccountManager) : AccountManagerFuture<Bundle> {
        //get account
        var account = getAccount(accountManager)
//        val accounts: Array<Account> = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE)
//        for (acc in accounts) {
//            //looking for the right type of account
//            if (acc.type.equals(Constants.ACCOUNT_TYPE, ignoreCase = true)) {
//                account = acc
//                break
//            }
//        }

        //update authtoken
        val authtoken: String? = accountManager.peekAuthToken(account, Constants.AUTH_TOKEN_TYPE)
        accountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE, authtoken)
        return accountManager.getAuthToken(account, Constants.AUTH_TOKEN_TYPE, null, false, null, null)
    }

    fun getAccount(accountManager: AccountManager): Account? {
        val accounts: Array<Account> = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE)
        for (acc in accounts) {
            //looking for the right type of account
            if (acc.type.equals(Constants.ACCOUNT_TYPE, ignoreCase = true)) {
                println("Account from helper $acc")
                return acc
            }
        }
        return null
    }

    fun getRefreshToken(accountManager: AccountManager): String {
        var account = getAccount(accountManager)
        println("Account:$account")
        return accountManager.getUserData(account, Constants.REFRESH_TOKEN)
    }
}