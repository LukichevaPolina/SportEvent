package com.sport.event.activities

import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sport.event.R
import com.sport.event.Constants
import com.sport.event.accountManager.AccountManagerHelper
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.models.Event
import com.sport.event.retrofit.models.EventRequest
import com.sport.event.retrofit.models.Sport
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class CreateEventFragment : Fragment() {
    var personNumber: Int = 5
    var cal = Calendar.getInstance(Locale.ENGLISH)
    private val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val day: Int = cal[Calendar.DAY_OF_MONTH]
    val month: Int = cal[Calendar.MONTH]
    var minute = (cal[Calendar.MINUTE] / 15 + 1) * 15
    var hour = cal[Calendar.HOUR_OF_DAY]

    var days = ArrayList((day..maxDaysInMonth).toList())
    var months = if (day == 1) Constants.month.slice(month..month) else Constants.month.slice(month..month+1)
    var minutes = ArrayList((minute..59 step 15).toList().map{String.format("%02d", it)})
    var hours = ArrayList((hour..23).toList().map{String.format("%02d", it)})

    lateinit var dayAdapter: ArrayAdapter<Int>
    lateinit var monthAdapter: ArrayAdapter<String>
    lateinit var hourAdapter: ArrayAdapter<String>
    lateinit var minuteAdapter: ArrayAdapter<String>
    lateinit var sportAdapter: ArrayAdapter<String>

    var selectedDay: Int = day
    var selectedMonth: Int = month
    var selectedHour:Int = hour
    var selectedMinute: Int = minute
    var selectedSport: Int = 1

    var sports = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_event, container, false)

        dayAdapter= ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, days)
        monthAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, months)
        hourAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, hours)
        minuteAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, minutes)
        sportAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, sports)

        APIApp.restClient?.service?.getSports()?.enqueue(object:
            Callback<ArrayList<Sport>> {
            override fun onResponse(call: Call<ArrayList<Sport>>, response: Response<ArrayList<Sport>>) {
                val sports_response = response.body()!!
                sports.clear()
                for (sport in sports_response){
                    sports.add(sport.sport)
                }
                sportAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ArrayList<Sport>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        val personNumberTextView: TextView = view.findViewById(R.id.quantity)
        val minusButton: Button = view.findViewById(R.id.button_minus)
        val plusButton: Button = view.findViewById(R.id.button_plus)

        minusButton.setOnClickListener {
            clickMinus(minusButton, plusButton, personNumberTextView)
        }
        plusButton.setOnClickListener {
            clickPlus(minusButton, plusButton, personNumberTextView)
        }

        setDaysSpinner(view)
        setMonthSpinner(view)
        setHoursSpinner(view)
        setMinuteSpinner(view)
        setSportSpinner(view)

        val createButton: Button = view.findViewById(R.id.btnCreate)
        createButton.setOnClickListener {
            CreateEvent()
        }

        val cancelButton: Button = view.findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener {
            CloseFragment()
        }

        return view
    }


    private fun setDaysSpinner(view: View) {
        val daysSpinner: Spinner = view.findViewById(R.id.spinner_day)
        dayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        daysSpinner.setAdapter(dayAdapter)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedDay = parent.getItemAtPosition(position) as Int
                    if (selectedDay == day && selectedMonth == month)
                    {
                        setTodayMinutes(minutes, minuteAdapter)
                        setTodayHours(hours, hourAdapter)
                    } else {
                        setStartMinutes(minutes, minuteAdapter)
                        setStartHours(hours, hourAdapter)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        daysSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setMonthSpinner(view: View) {
        val monthSpinner: Spinner = view.findViewById(R.id.spinner_month)
        monthAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        monthSpinner.setAdapter(monthAdapter)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedMonth = Constants.month.indexOf(parent.getItemAtPosition(position))
                    if (selectedMonth == month) {
                        setMiddleMonth(days, dayAdapter)
                    }
                    else {
                        setStartMonth(days, dayAdapter)
                    }
                    if (selectedDay == day && selectedMonth == month)
                    {
                        setTodayMinutes(minutes, minuteAdapter)
                        setTodayHours(hours, hourAdapter)
                    } else {
                        setStartMinutes(minutes, minuteAdapter)
                        setStartHours(hours, hourAdapter)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        monthSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setMinuteSpinner(view: View) {
        val minuteSpinner: Spinner = view.findViewById(R.id.spinner_minutes)
        minuteAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        minuteSpinner.setAdapter(minuteAdapter)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedMinute = (parent.getItemAtPosition(position) as String).toInt()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        minuteSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setHoursSpinner(view: View) {
        val hourSpinner: Spinner = view.findViewById(R.id.spinner_hours)
        hourAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        hourSpinner.setAdapter(hourAdapter)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedHour = (parent.getItemAtPosition(position) as String).toInt()

                    if (selectedHour == hour) {
                        setTodayMinutes(minutes, minuteAdapter)
                    } else {
                        setStartMinutes(minutes, minuteAdapter)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        hourSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setSportSpinner(view: View) {
        val sportSpinner: Spinner = view.findViewById(R.id.sport_spinner)
        sportAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        sportSpinner.setAdapter(sportAdapter)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                        selectedSport = position + 1
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        sportSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setStartMonth(data: ArrayList<Int>, adapter: ArrayAdapter<Int>) {
        data.clear()
        data.addAll(ArrayList((1..day - 1).toList()))
        adapter.notifyDataSetChanged()
    }

    private fun setMiddleMonth(data: ArrayList<Int>, adapter: ArrayAdapter<Int>) {
        data.clear()
        data.addAll(ArrayList((day..maxDaysInMonth).toList()))
        adapter.notifyDataSetChanged()
    }

    private fun setStartMinutes(data: ArrayList<String>, adapter: ArrayAdapter<String>) {
        data.clear()
        data.addAll(ArrayList((0..59 step 15).toList().map{String.format("%02d", it)}))
        adapter.notifyDataSetChanged()
    }

    private fun setTodayMinutes(data: ArrayList<String>, adapter: ArrayAdapter<String>) {
        cal = Calendar.getInstance(Locale.ENGLISH)
        minute = (cal[Calendar.MINUTE] / 15 + 1) * 15
        data.clear()
        data.addAll(ArrayList((minute..59 step 15).toList().map{String.format("%02d", it)}))
        adapter.notifyDataSetChanged()
    }

    private fun setStartHours(data: ArrayList<String>, adapter: ArrayAdapter<String>) {
        data.clear()
        data.addAll(ArrayList((0..23).toList().map{String.format("%02d", it)}))
        adapter.notifyDataSetChanged()
    }

    private fun setTodayHours(data: ArrayList<String>, adapter: ArrayAdapter<String>) {
        cal = Calendar.getInstance(Locale.ENGLISH)
        hour = cal[Calendar.HOUR_OF_DAY]
        data.clear()
        data.addAll(ArrayList((hour..23).toList().map{String.format("%02d", it)}))
        adapter.notifyDataSetChanged()
    }

    private fun clickMinus(buttonMinus: Button, buttonPlus: Button, textView: TextView) {
        if (personNumber == 2) {
            buttonMinus.isEnabled = false
        }
        buttonPlus.isEnabled = true
        personNumber -= 1
        textView.text = personNumber.toString()
    }

    private fun clickPlus(buttonMinus: Button, buttonPlus: Button, textView: TextView) {
        if (personNumber == 9) {
            buttonPlus.isEnabled = false
        }
        buttonMinus.isEnabled = true
        personNumber += 1
        textView.text = personNumber.toString()
    }


    private fun CreateEvent() {
        val date = "2022-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d",selectedDay)
        val startTime = selectedHour.toString() + ":" + selectedMinute
        val endTime = (selectedHour + 1).toString() + ":" + selectedMinute
        val freeSeats = personNumber
        val level = 1
        val latitude = 22
        val longitude = 22
        val new_event = EventRequest(selectedSport, date, startTime, endTime, personNumber, freeSeats, level, latitude, longitude)

        val accountManager = AccountManager.get(context)
        val future: AccountManagerFuture<Bundle> = AccountManagerHelper().getFutureUpdateToken(accountManager)

        Thread {
            val authToken = future.result.getString(AccountManager.KEY_AUTHTOKEN)
            APIApp.restClient?.service?.addEvent("Bearer " + authToken, new_event)?.enqueue(object:
                Callback<Event> {
                override fun onResponse(
                    call: Call<Event>,
                    response: Response<Event>
                ) {
                    Toast.makeText(context, "Событие успешно добавлено!", Toast.LENGTH_LONG).show()
                    CloseFragment()
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    t.printStackTrace()
                    println("error")
                }
            })
        }.start()
    }

    fun CloseFragment() {
        val fm: FragmentManager = parentFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.remove(this)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        ft.commit()
    }

    companion object {
        fun newInstance(param1: String, param2: String) = CreateEventFragment()
    }
}