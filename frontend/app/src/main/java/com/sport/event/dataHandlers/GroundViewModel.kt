package com.sport.event.dataHandlers

import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sport.event.accountManager.AccountManagerHelper
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.models.Event
import com.sport.event.retrofit.models.Ground
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroundViewModel : ViewModel() {
    val groundsList = MutableLiveData<ArrayList<Ground>>()


    fun getGrounds(accountManager: AccountManager) {
        val future: AccountManagerFuture<Bundle> = AccountManagerHelper().getFutureUpdateToken(accountManager)
        Thread {
            val authToken = future.result.getString(AccountManager.KEY_AUTHTOKEN)
            APIApp.restClient?.service?.getGrounds("Bearer " + authToken)?.enqueue(object:
                Callback<ArrayList<Ground>> {
                override fun onResponse(call: Call<ArrayList<Ground>>, response: Response<ArrayList<Ground>>) {
                    groundsList.postValue(response.body())

                }

                override fun onFailure(call: Call<ArrayList<Ground>>, t: Throwable) {
                }
            })
        }.start()
    }
}