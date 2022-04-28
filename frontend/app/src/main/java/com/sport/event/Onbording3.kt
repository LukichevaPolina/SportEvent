package com.sport.event

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.accounts.AccountManagerFuture

import android.accounts.AccountManagerCallback
import android.util.Log
import java.lang.Exception


class Onbording3 : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onbording3)
        button = findViewById(R.id.btnNext)
        button.setOnClickListener() {
            val accountManager: AccountManager = AccountManager.get(this@Onbording3)

            //accountManager.addAccount opens AuthenticationActivity which creates account on device
            //future needs for debug
            val future : AccountManagerFuture<Bundle> = accountManager.addAccount(AccountUtils.ACCOUNT_TYPE, AccountUtils.ARG_AUTH_TOKEN_TYPE,null,  null, this, object: AccountManagerCallback<Bundle>{
                override fun run(future : AccountManagerFuture<Bundle> ) {
                    try {
                        val bnd: Bundle  = future.getResult()
                        Log.d("SportEvent", "AddNewAccount Bundle is " + bnd);

                    } catch (e: Exception) {
                        e.printStackTrace();
                        println(e)
                    }
                }
            }, null)

            //Maps activity should opens after successful auth, but I think it happens earlier,
            //because it located on MainTread, but Request to server on other thread
            // TODO: think about sync
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun accountAvailable(): Account? {
        //get accountManager
        val accountManager: AccountManager = AccountManager.get(this@Onbording3)
        //get array of Accounts from Account manager
        val accounts: Array<Account> = accountManager.getAccountsByType(AccountUtils.ACCOUNT_TYPE)
        for (acc in accounts) {
            //check that account SportEvent contains in accountManager
            if (acc.name.equals(AccountUtils.ACCOUNT_TYPE, ignoreCase = true)) {
                return acc
            }
        }
        return null
    }
}
