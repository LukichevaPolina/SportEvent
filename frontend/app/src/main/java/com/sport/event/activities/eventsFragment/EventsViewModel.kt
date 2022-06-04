package com.sport.event.activities.eventsFragment

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
import com.sport.event.accountManager.AccountManagerHelper
import com.sport.event.retrofit.models.EventFilters


class EventsViewModel : ViewModel() {
    val eventList = MutableLiveData<ArrayList<Event>>()

    fun getEventsDate(date: String?, accountManager: AccountManager) {
        val future: AccountManagerFuture<Bundle> = AccountManagerHelper().getFutureUpdateToken(accountManager)
        Thread {
            val authToken = future.result.getString(AccountManager.KEY_AUTHTOKEN)
            APIApp.restClient?.service?.getEventsDate("Bearer " + authToken, date)?.enqueue(object: Callback<ArrayList<Event>> {
                override fun onResponse(call: Call<ArrayList<Event>>, response: Response<ArrayList<Event>>) {
                    eventList.postValue(response.body())
                }

                override fun onFailure(call: Call<ArrayList<Event>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }.start()
    }

    fun getFilteredEvents(sport: Int?,
                          date: String?,
                          start_time:String?,
                          free_seats_gte: Int?,
                          free_seats_lte: Int?,
                          accountManager: AccountManager) {
        val future: AccountManagerFuture<Bundle> = AccountManagerHelper().getFutureUpdateToken(accountManager)
        Thread {
            val authToken = future.result.getString(AccountManager.KEY_AUTHTOKEN)
            APIApp.restClient?.service?.eventsFilters("Bearer $authToken", sport, date, start_time, free_seats_gte, free_seats_lte )?.enqueue(object: Callback<ArrayList<Event>> {
                override fun onResponse(call: Call<ArrayList<Event>>, response: Response<ArrayList<Event>>) {
                    eventList.postValue(response.body())
                }

                override fun onFailure(call: Call<ArrayList<Event>>, t: Throwable) {
                    t.printStackTrace()
                }
            })

        }.start()
    }
}
