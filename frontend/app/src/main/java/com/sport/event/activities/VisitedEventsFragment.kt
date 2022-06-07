package com.sport.event.activities

import android.accounts.AccountManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.sport.event.R
import com.sport.event.dataHandlers.EventsViewModel
import com.sport.event.retrofit.FullEventsAdapter

class VisitedEventsFragment(isVisitedFragment: Boolean) : Fragment() {
    val isVisitedF = isVisitedFragment
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: FullEventsAdapter

    private val viewModel: EventsViewModel by lazy {
        ViewModelProvider(this).get(EventsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_visited_events, container, false)

        eventsRecyclerView = view.findViewById(R.id.events_recycler_view)

        eventsAdapter = FullEventsAdapter(view.findViewById(R.id.loadingPanel))
        eventsRecyclerView.adapter = eventsAdapter
        viewModel.eventList.observe(viewLifecycleOwner, Observer {
            eventsAdapter.setEventList(it)
        })

        val buttonBack: ImageButton = view.findViewById(R.id.icon_back)
        buttonBack.setOnClickListener{
            val fm: FragmentManager = parentFragmentManager
            val ft: FragmentTransaction = fm.beginTransaction()
            ft.remove(this)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            ft.commit()
        }

        val topText: TextView = view.findViewById(R.id.topText)
        val accountManager = AccountManager.get(context)
        if (isVisitedF) {
            viewModel.getVisitedEvents(accountManager)
        } else {
            topText.text = "Созданные события"
            viewModel.getCreatedEvents(accountManager)
        }

        return view
    }

}