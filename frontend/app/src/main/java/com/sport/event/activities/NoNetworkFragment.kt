package com.sport.event.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sport.event.R
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.models.Sport
import retrofit2.Call
import retrofit2.Response

class NoNetworkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_no_network, container, false)

        val button: Button = view.findViewById(R.id.button)
        button.setOnClickListener{
            APIApp.restClient?.service?.getSports()?.enqueue(object :
                    retrofit2.Callback<ArrayList<Sport>> {
                    override fun onResponse(
                        call: Call<ArrayList<Sport>>,
                        response: Response<ArrayList<Sport>>
                    ) {
                        closeFragment()
                    }

                    override fun onFailure(call: Call<ArrayList<Sport>>, t: Throwable) {}
                })
        }

        return view
    }

    private fun closeFragment() {
        val fm: FragmentManager = parentFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.remove(this)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        ft.commit()
    }
}