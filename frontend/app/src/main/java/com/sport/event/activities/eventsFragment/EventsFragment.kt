package com.sport.event.activities.eventsFragment

import android.accounts.AccountManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sport.event.R
import java.text.SimpleDateFormat
import java.util.*

class EventsFragment : Fragment() {

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    // date you are currently in
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    // will use to set the current day, month, and year
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    // will use as the currently selected date
    private var selectedDay: Int = currentDay
    // ArrayList of all days in one month
    private val dates = ArrayList<Date>()

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventsRecyclerView: RecyclerView
    val eventsAdapter: EventsAdapter = EventsAdapter()

    private lateinit var accountManager: AccountManager

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

        calendarRecyclerView = view.findViewById(R.id.calendar_recycler_view)
        //This is the maximum month that the calendar will display.

        setUpCalendar()

        eventsRecyclerView = view.findViewById(R.id.events_recycler_view)
        eventsRecyclerView.adapter = eventsAdapter
        viewModel.eventList.observe(viewLifecycleOwner, Observer {
            eventsAdapter.setEventList(it)
        })

        //get accountManager
        accountManager = AccountManager.get(context)
        val date: String? = sdf.format(cal.time)
        viewModel.getEventsDate(date, accountManager)

        return view
    }

    private fun setUpCalendar(changeMonth: Calendar? = null) {

        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val date = currentDate.clone() as Calendar
        selectedDay = currentDay

        dates.clear()

        while (dates.size < maxDaysInMonth) {
            dates.add(date.time)
            date.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Assigning calendar view.
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        calendarRecyclerView.layoutManager = layoutManager
        calendarAdapter = context?.let { CalendarAdapter(it, dates, currentDate, changeMonth) }!!
        calendarRecyclerView.adapter = calendarAdapter



        //update eventsRecyclerView when data in calendar change
        calendarAdapter.setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickCalendar = Calendar.getInstance()
                clickCalendar.time = dates[position]
                selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]
                val selectedDayRequest = sdf.format(dates[position])
                viewModel.getEventsDate(selectedDayRequest, accountManager)
                eventsRecyclerView.scrollToPosition(0)
            }
        })
    }
}