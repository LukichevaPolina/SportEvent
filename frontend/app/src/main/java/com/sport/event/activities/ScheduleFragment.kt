package com.sport.event.activities

import android.accounts.AccountManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sport.event.Constants
import com.sport.event.R
import androidx.lifecycle.Observer
import com.sport.event.activities.eventsFragment.CalendarAdapter
import com.sport.event.activities.eventsFragment.EventsAdapter
import com.sport.event.activities.eventsFragment.EventsViewModel
import java.text.SimpleDateFormat
import java.util.*

class ScheduleFragment : Fragment(), EventsAdapter.Communicator {
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

    private lateinit var date: String
    private lateinit var selectedDate: String

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter

    private lateinit var accountManager: AccountManager

    private val viewModel: EventsViewModel by lazy {
        ViewModelProvider(this).get(EventsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        date = sdf.format(cal.time)
        selectedDate = date

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)

        val todayText: TextView = view.findViewById(R.id.today_text)
        val today = Constants.fullDaysOfWeek[cal[Calendar.DAY_OF_WEEK] - 1] + ", " + cal[Calendar.DAY_OF_MONTH] + ' ' + Constants.monthRP[cal[Calendar.MONTH]]
        todayText.text = today

        calendarRecyclerView = view.findViewById(R.id.calendar_recycler_view)
        //This is the maximum month that the calendar will display.

        setUpCalendar()

        eventsRecyclerView = view.findViewById(R.id.events_recycler_view)

        eventsAdapter= EventsAdapter(view.findViewById(R.id.loadingPanel), this)
        eventsRecyclerView.adapter = eventsAdapter
        viewModel.eventList.observe(viewLifecycleOwner, Observer {
            eventsAdapter.setEventList(it)
        })



        //get accountManager
        accountManager = AccountManager.get(context)
        viewModel.getScheduleDate(date, accountManager)

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
                selectedDate = sdf.format(dates[position])
                viewModel.getScheduleDate(selectedDayRequest, accountManager)
                eventsRecyclerView.scrollToPosition(0)
            }
        })
    }

    override fun updateSchedule() {
        println(sdf.format(selectedDay))
        viewModel.getScheduleDate(selectedDate, accountManager)
        eventsRecyclerView.scrollToPosition(0)
    }
}
