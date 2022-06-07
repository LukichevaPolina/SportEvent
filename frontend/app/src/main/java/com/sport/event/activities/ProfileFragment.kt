package com.sport.event.activities

import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sport.event.R
import com.sport.event.accountManager.AccountManagerHelper
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.models.LogoutRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val exit: TextView = view.findViewById(R.id.exit)
        exit.setOnClickListener {
            logout()
        }
        val eventsLayout: ConstraintLayout = view.findViewById(R.id.events)
        eventsLayout.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.container, VisitedEventsFragment(true), "VISITED_TAG").commit()
        }

        val myEventsLayout: ConstraintLayout = view.findViewById(R.id.my_events)
        myEventsLayout.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.container, VisitedEventsFragment(false), "CREATED_TAG").commit()
        }
        return view
    }

    private fun logout() {
        val accountManager = AccountManager.get(context)
        val refreshToken = AccountManagerHelper().getRefreshToken(accountManager)
        val future: AccountManagerFuture<Bundle> = AccountManagerHelper().getFutureUpdateToken(
            accountManager)
        Thread {
            val authToken = future.result.getString(AccountManager.KEY_AUTHTOKEN)
            val logoutRequest = LogoutRequest(refreshToken)
            val account = AccountManagerHelper().getAccount(accountManager)
            accountManager.removeAccount(account, activity, null, null)
            APIApp.restClient?.service?.logoutUser("Bearer $authToken", logoutRequest)?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    startStartScreen()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }.start()
    }

    private fun startStartScreen() {
        val intent = Intent(activity, StartScreen::class.java)
        startActivity(intent)
        activity?.finish()
    }
}
