package com.sport.event.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sport.event.Constants
import com.sport.event.R
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.models.EventFilters
import com.sport.event.retrofit.models.Sport
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.*
import kotlin.collections.ArrayList

class FilterFragment :  Fragment() {
    lateinit var communicator: FilterFragment.FragmentCommunicator

    private var cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    private val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    private val day: Int = cal[Calendar.DAY_OF_MONTH]
    private val month: Int = cal[Calendar.MONTH]
    private var minute = ((cal[Calendar.MINUTE] / 15) + 1) * 15 % 60
    private var hour = cal[Calendar.HOUR_OF_DAY]

    private var days = ArrayList((day..maxDaysInMonth).toList().map{it.toString()})
    private var months = if (day == 1) ArrayList(Constants.month.slice(month..month).map{it.toString()})
                         else ArrayList(Constants.month.slice(month..month+1).map{it.toString()})
    private var startMinutes = ArrayList((minute..59 step 15).toList().map{String.format("%02d", it)})
    private var startHours = ArrayList((hour..23).toList().map{String.format("%02d", it)})
    private var sports = ArrayList<String>()
    private var sportsId = ArrayList<Int>()
    private var personQuantity = ArrayList((1..10).toList())

    lateinit var dayAdapter: ArrayAdapter<String>
    lateinit var monthAdapter: ArrayAdapter<String>
    lateinit var startHourAdapter: ArrayAdapter<String>
    lateinit var startMinuteAdapter: ArrayAdapter<String>
    lateinit var sportAdapter: ArrayAdapter<String>
    lateinit var fromAdapter: ArrayAdapter<Int>
    lateinit var toAdapter: ArrayAdapter<Int>


    private var selectedDay: String = "-"
    private var selectedMonth: String = "-"
    private var selectedStartHour:String = "-"
    private var selectedStartMinute: String = "-"

    private var selectedSport: Int? = 0

    private var selectedFrom: Int = 1
    private var selectedTo: Int = 1

    private var is_filter = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter_events, container, false)

        days.add("-")
        months.add("-")
        startHours.add("-")
        startMinutes.add("-")

        dayAdapter= ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, days)
        monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, months)
        startHourAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, startHours)
        startMinuteAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, startMinutes)
        sportAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sports)
        fromAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, personQuantity)
        toAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, personQuantity)

        APIApp.restClient?.service?.getSports()?.enqueue(object:
            Callback<ArrayList<Sport>> {
            override fun onResponse(call: Call<ArrayList<Sport>>, response: Response<ArrayList<Sport>>) {
                val sports_response = response.body()!!
                sports.clear()
                for (sport in sports_response){
                    sports.add(sport.sport)
                    sportsId.add(sport.id)
                }
                sports.add("-")
                sportsId.add(0)
                sportAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ArrayList<Sport>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        setDaysSpinner(view)
        setMonthSpinner(view)
        setStartHoursSpinner(view)
        setStartMinuteSpinner(view)
        setSportSpinner(view)
        setFromSpinner(view)
        setToSpinner(view)

        val createButton: Button = view.findViewById(R.id.btnCreate)
        createButton.setOnClickListener {
            is_filter = true
            CloseFragment()
        }

        val cancelButton: Button = view.findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener {
            CloseFragment()
        }

        return view
    }

    private fun setDaysSpinner(view: View) {
        val daysSpinner: Spinner = view.findViewById(R.id.spinner_day)
        daysSpinner.adapter = dayAdapter
        daysSpinner.setSelection(days.size - 1)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedDay = parent.getItemAtPosition(position) as String

                    if (selectedMonth == month.toString() && selectedDay == day.toString()) {
                        setTodayHours(startHourAdapter)
                        if (selectedStartHour == hour.toString()) {
                            setTodayMinutes(startMinuteAdapter)
                        }
                    } else {
                        setStartHours(startHourAdapter)
                        setStartMinutes(startMinuteAdapter)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        daysSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setMonthSpinner(view: View) {
        val monthSpinner: Spinner = view.findViewById(R.id.spinner_month)
        monthSpinner.adapter = monthAdapter
        monthSpinner.setSelection(months.size - 1)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedMonth = parent.getItemAtPosition(position) as String

                    selectedMonth = Constants.month.indexOf(selectedMonth).toString()
                    if (selectedMonth == month.toString()) {
                        setMiddleDays(dayAdapter)
                        if (selectedDay == day.toString()) {
                            setTodayHours(startHourAdapter)
                            if (selectedStartHour == hour.toString()) {
                                setTodayMinutes(startMinuteAdapter)
                            } else {
                                setStartMinutes(startMinuteAdapter)
                            }
                        } else {
                            setStartHours(startHourAdapter)
                            setStartMinutes(startMinuteAdapter)
                        }
                    } else {
                        setStartDays(dayAdapter)
                        setStartHours(startHourAdapter)
                        setStartMinutes(startMinuteAdapter)
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        monthSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setStartMinuteSpinner(view: View) {
        val minuteSpinner: Spinner = view.findViewById(R.id.spinner_start_minutes)
        minuteSpinner.adapter = startMinuteAdapter
        minuteSpinner.setSelection(startMinutes.size - 1)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedStartMinute = parent.getItemAtPosition(position) as String
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        minuteSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setStartHoursSpinner(view: View) {
        val hourSpinner: Spinner = view.findViewById(R.id.spinner_start_hours)
        hourSpinner.adapter = startHourAdapter
        hourSpinner.setSelection(startHours.size - 1)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedStartHour = parent.getItemAtPosition(position) as String

                    if (selectedStartHour == hour.toString() && selectedDay == day.toString() && selectedMonth == month.toString()) {
                        setTodayMinutes(startMinuteAdapter)
                    } else {
                        setStartMinutes(startMinuteAdapter)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        hourSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setSportSpinner(view: View) {
        val sportSpinner: Spinner = view.findViewById(R.id.sport_spinner)
        sportSpinner.adapter = sportAdapter
        sportSpinner.setSelection(sports.size - 1)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedSport = sportsId[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        sportSpinner.onItemSelectedListener = itemSelectedListener
    }

    private fun setFromSpinner(view: View) {
        val fromSpinner: Spinner = view.findViewById(R.id.spinner_from)
        fromSpinner.adapter = fromAdapter

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedFrom = parent.getItemAtPosition(position) as Int
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        fromSpinner.onItemSelectedListener = itemSelectedListener
    }

    private fun setToSpinner(view: View) {
        val toSpinner: Spinner = view.findViewById(R.id.spinner_to)
        toSpinner.adapter = toAdapter
        toSpinner.setSelection(personQuantity.size - 1)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedTo = parent.getItemAtPosition(position) as Int
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        toSpinner.onItemSelectedListener = itemSelectedListener
    }

    private fun CloseFragment() {
        val fm: FragmentManager = parentFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.remove(this)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        ft.commit()
    }

    private fun setStartDays(adapter: ArrayAdapter<String>) {
        adapter.clear()
        adapter.addAll(ArrayList((1 until day).toList()).map{it.toString()})
        adapter.add("-")
        if (selectedDay == "-") {
            view?.findViewById<Spinner>(R.id.spinner_day)?.setSelection(day)
        }
        else {
            view?.findViewById<Spinner>(R.id.spinner_day)?.setSelection(0)
            selectedDay = 1.toString()
        }
    }

    private fun setMiddleDays(adapter: ArrayAdapter<String>) {
        adapter.clear()
        adapter.addAll(ArrayList((day..maxDaysInMonth).toList()).map{it.toString()})
        adapter.add("-")
        if (selectedDay == "-") {
            view?.findViewById<Spinner>(R.id.spinner_day)?.setSelection(maxDaysInMonth - day)
        }
        else {
            view?.findViewById<Spinner>(R.id.spinner_day)?.setSelection(0)
            selectedDay = 1.toString()
        }
    }

    private fun setStartMinutes(adapter: ArrayAdapter<String>) {
        adapter.clear()
        val minutes = ArrayList((0..59 step 15).toList().map{String.format("%02d", it)})
        adapter.addAll(minutes)
        adapter.add("-")
        if (selectedStartMinute == "-") {
            view?.findViewById<Spinner>(R.id.spinner_start_minutes)?.setSelection(minutes.size)
            selectedStartMinute = "-"
        } else {
            view?.findViewById<Spinner>(R.id.spinner_start_minutes)?.setSelection(0)
            selectedStartMinute = 0.toString()
        }
    }

    private fun setTodayMinutes(adapter: ArrayAdapter<String>) {
        cal = Calendar.getInstance(Locale.ENGLISH)
        minute = (cal[Calendar.MINUTE] / 15 + 1) * 15 % 60

        val minutes = if (minute == 0) {
            ArrayList((0..59 step 15).toList().map{String.format("%02d", it)})
        } else {
            ArrayList((minute..59 step 15).toList().map{String.format("%02d", it)})
        }
        adapter.clear()
        adapter.addAll(minutes)
        adapter.add("-")
        if (selectedStartMinute == "-") {
            view?.findViewById<Spinner>(R.id.spinner_start_minutes)?.setSelection(minutes.size)
            selectedStartMinute = "-"
        } else {
            view?.findViewById<Spinner>(R.id.spinner_start_minutes)?.setSelection(0)
            selectedStartMinute = 0.toString()
        }
    }

    private fun setStartHours(adapter: ArrayAdapter<String>) {
        adapter.clear()
        adapter.addAll(ArrayList((0..23).toList().map{String.format("%02d", it)}))
        adapter.add("-")
        if (selectedStartHour == "-") {
            view?.findViewById<Spinner>(R.id.spinner_start_hours)?.setSelection(24)
        } else {
            view?.findViewById<Spinner>(R.id.spinner_start_hours)?.setSelection(9)
        }
    }

    private fun setTodayHours(adapter: ArrayAdapter<String>) {
        cal = Calendar.getInstance(Locale.ENGLISH)
        hour = cal[Calendar.HOUR_OF_DAY]
        val hours = if (minute == 0) {
            ArrayList(((hour + 1)..23).toList().map{String.format("%02d", it)})
        } else {
            ArrayList((hour..23).toList().map{String.format("%02d", it)})
        }
        adapter.clear()
        adapter.addAll(hours)
        adapter.add("-")
        if (selectedStartHour == "-") {
            view?.findViewById<Spinner>(R.id.spinner_start_hours)?.setSelection(hours.size)
        }
        else {
            view?.findViewById<Spinner>(R.id.spinner_start_hours)?.setSelection(0)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is FilterFragment.FragmentCommunicator)
            communicator = parentFragment as FilterFragment.FragmentCommunicator
    }

    override fun onDetach() {
        super.onDetach()
//        val date = if (selectedDay == "-") null else "2022-" + String.format("%02d", selectedMonth.toInt() + 1) + "-" + String.format("%02d",selectedDay)
        val date = null
        val startTime = if (selectedStartHour == "-") null else selectedStartHour + ":" + selectedStartMinute
        if (selectedSport == 0) selectedSport = null
        val eventfilters = EventFilters(selectedSport, date, startTime, selectedFrom, selectedTo)
        communicator.fragmentDetached(is_filter, selectedSport, date, startTime, selectedFrom, selectedTo)
    }

    interface FragmentCommunicator {
        fun fragmentDetached(is_filter:Boolean,
                             sport: Int?,
                             date: String?,
                             start_time:String?,
                             free_seats_gte: Int?,
                             free_seats_lte: Int?)
    }
}