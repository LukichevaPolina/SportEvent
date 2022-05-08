package com.sport.event.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sport.event.R

class EventsFragment : Fragment() {

    //method for creating new instances of the fragment, a factory method
    companion object {
        fun newInstance(): EventsFragment {
            return EventsFragment()
        }
    }

    //creates the view for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false)
    }
}