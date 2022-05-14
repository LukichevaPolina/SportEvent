package com.sport.event.activities.eventsFragment

import android.accounts.AccountManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.sport.event.R

class EventsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    val adapter: EventsAdapter = EventsAdapter()

    private val viewModel: EventsViewModel by lazy {
        ViewModelProvider(this).get(EventsViewModel::class.java)
    }

    //creates the view for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_events, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = adapter
        viewModel.eventList.observe(viewLifecycleOwner, Observer {
            adapter.setEventList(it)
        })

        //get accountManager
        val accountManager: AccountManager = AccountManager.get(context)

        viewModel.getEvents(accountManager)
        return view
    }

    //method for creating new instances of the fragment, a factory method
    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }
}