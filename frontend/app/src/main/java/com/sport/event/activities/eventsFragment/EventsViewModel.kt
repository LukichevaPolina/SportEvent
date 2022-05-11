package com.sport.event.activities.eventsFragment

import android.accounts.Account
import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.models.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Bundle

import android.accounts.AccountManagerFuture
import java.lang.Exception


class EventsViewModel : ViewModel() {
    val eventList = MutableLiveData<ArrayList<Event>>()
    val errorMessage = MutableLiveData<String>()

    fun getEvents(accountManager: AccountManager) {

        //get account
        lateinit var account: Account
        val accounts: Array<Account> = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE)
        for (acc in accounts) {
            //looking for the right type of account
            if (acc.type.equals(Constants.ACCOUNT_TYPE, ignoreCase = true)) {
                account = acc
                break
            }
        }

        //update authtoken
        var authtoken: String? = accountManager.peekAuthToken(account, Constants.AUTH_TOKEN_TYPE)
        accountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE, authtoken)
        val future: AccountManagerFuture<Bundle> =
            accountManager.getAuthToken(account, Constants.AUTH_TOKEN_TYPE, null, false, null, null)

        //get events
        Thread {
            try {
                val bnd = future.result
                authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN)
                println(authtoken)
                APIApp.restClient?.service?.getEvents("Bearer " + authtoken)?.enqueue(object: Callback<ArrayList<Event>> {
                    override fun onResponse(
                        call: Call<ArrayList<Event>>,
                        response: Response<ArrayList<Event>>
                    ) {
                        eventList.postValue(response.body())
                    }

                    override fun onFailure(call: Call<ArrayList<Event>>, t: Throwable) {
                        errorMessage.postValue(t.message)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}



