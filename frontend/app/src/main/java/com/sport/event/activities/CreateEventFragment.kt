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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
import java.util.Collections.copy
import kotlin.collections.ArrayList

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import java.lang.RuntimeException


class CreateEventFragment : Fragment(), OnMapReadyCallback {

    lateinit var communicator: FragmentCommunicator

    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null

    private var personNumber: Int = 5
    private var cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
    private val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    private val day: Int = cal[Calendar.DAY_OF_MONTH]
    private val month: Int = cal[Calendar.MONTH]
    private var minute = ((cal[Calendar.MINUTE] / 15) + 1) * 15 % 60
    private var hour = cal[Calendar.HOUR_OF_DAY]

    private var days = ArrayList((day..maxDaysInMonth).toList())
    private var months = if (day == 1) Constants.month.slice(month..month) else Constants.month.slice(month..month+1)
    private var startMinutes = ArrayList((minute..59 step 15).toList().map{String.format("%02d", it)})
    private var startHours = ArrayList((hour..23).toList().map{String.format("%02d", it)})
    private var endHours = startHours
    private var endMinutes = startMinutes

    lateinit var dayAdapter: ArrayAdapter<Int>
    lateinit var monthAdapter: ArrayAdapter<String>
    lateinit var startHourAdapter: ArrayAdapter<String>
    lateinit var startMinuteAdapter: ArrayAdapter<String>
    lateinit var endHourAdapter: ArrayAdapter<String>
    lateinit var endMinuteAdapter: ArrayAdapter<String>
    lateinit var sportAdapter: ArrayAdapter<String>

    private var selectedDay: Int = day
    private var selectedMonth: Int = month
    private var selectedStartHour:Int = hour
    private var selectedStartMinute: Int = minute
    private var selectedEndHour:Int = hour
    private var selectedEndMinute: Int = minute
    private var selectedSport: Int = 1
    private var selectedLatitude: Double = .1
    private var selectedLongitude: Double = .1
    private var selectedAddress: String = "пл. Минина и Пожарского, 1"

    private var sports = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_create_event, container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        dayAdapter= ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, days)
        monthAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, months)
        startHourAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, startHours)
        startMinuteAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, startMinutes)
        endHourAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, endHours)
        endMinuteAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, endMinutes)
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
        setStartHoursSpinner(view)
        setStartMinuteSpinner(view)
        setEndHoursSpinner(view)
        setEndMinuteSpinner(view)
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

                    if (selectedMonth == month && selectedDay == day) {
                        setTodayHours(startHourAdapter, endHourAdapter)
                        if (selectedStartHour == hour) {
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
                        setMiddleDays(dayAdapter)
                        if (selectedDay == day) {
                            setTodayHours(startHourAdapter, endHourAdapter)
                            if (selectedStartHour == hour) {
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
        minuteSpinner.setAdapter(startMinuteAdapter)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedStartMinute = (parent.getItemAtPosition(position) as String).toInt()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        minuteSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setEndMinuteSpinner(view: View) {
        val minuteSpinner: Spinner = view.findViewById(R.id.spinner_end_minutes)
        minuteSpinner.setAdapter(endMinuteAdapter)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedEndMinute = (parent.getItemAtPosition(position) as String).toInt()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        minuteSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setStartHoursSpinner(view: View) {
        val hourSpinner: Spinner = view.findViewById(R.id.spinner_start_hours)
        startHourAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        hourSpinner.setAdapter(startHourAdapter)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedStartHour = (parent.getItemAtPosition(position) as String).toInt()

                    if (selectedStartHour == hour && selectedDay == day && selectedMonth == month) {
                        setTodayMinutes(startMinuteAdapter)
                    } else {
                        setStartMinutes(startMinuteAdapter)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        hourSpinner.setOnItemSelectedListener(itemSelectedListener)
    }

    private fun setEndHoursSpinner(view: View) {
        val hourSpinner: Spinner = view.findViewById(R.id.spinner_end_hours)
        hourSpinner.setAdapter(endHourAdapter)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedEndHour = (parent.getItemAtPosition(position) as String).toInt()

                    if (selectedEndHour == hour && selectedDay == day && selectedMonth == month) {
                        setTodayMinutes(startMinuteAdapter)
                    }
                    else {
                        setStartEndMinutes(endMinuteAdapter)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        hourSpinner.onItemSelectedListener = itemSelectedListener
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
        sportSpinner.onItemSelectedListener = itemSelectedListener
    }

    private fun setStartDays(adapter: ArrayAdapter<Int>) {
        adapter.clear()
        adapter.addAll(ArrayList((1 until day).toList()))
        view?.findViewById<Spinner>(R.id.spinner_day)?.setSelection(0)
        selectedDay = 1
    }

    private fun setMiddleDays(adapter: ArrayAdapter<Int>) {
        adapter.clear()
        adapter.addAll(ArrayList((day..maxDaysInMonth).toList()))
        view?.findViewById<Spinner>(R.id.spinner_day)?.setSelection(0)
        selectedDay = day
    }

    private fun setStartMinutes(adapter: ArrayAdapter<String>) {
        adapter.clear()
        val minutes = ArrayList((0..59 step 15).toList().map{String.format("%02d", it)})
        adapter.addAll(minutes)
        view?.findViewById<Spinner>(R.id.spinner_start_minutes)?.setSelection(0)
        view?.findViewById<Spinner>(R.id.spinner_end_minutes)?.setSelection(0)

        selectedStartMinute = 0
    }

    private fun setStartEndMinutes(adapter: ArrayAdapter<String>) {
        adapter.clear()
        adapter.addAll(ArrayList((0..59 step 15).toList().map{String.format("%02d", it)}))
        view?.findViewById<Spinner>(R.id.spinner_end_minutes)?.setSelection(0)
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
        view?.findViewById<Spinner>(R.id.spinner_start_minutes)?.setSelection(0)
        view?.findViewById<Spinner>(R.id.spinner_end_minutes)?.setSelection(0)
    }

    private fun setStartHours(adapter: ArrayAdapter<String>) {
        adapter.clear()
        adapter.addAll(ArrayList((0..23).toList().map{String.format("%02d", it)}))
        view?.findViewById<Spinner>(R.id.spinner_start_hours)?.setSelection(9)
        view?.findViewById<Spinner>(R.id.spinner_end_hours)?.setSelection(9)
    }

    private fun setTodayHours(startAdapter: ArrayAdapter<String>, endAdapter: ArrayAdapter<String>) {
        cal = Calendar.getInstance(Locale.ENGLISH)
        hour = cal[Calendar.HOUR_OF_DAY]
        val hours = if (minute == 0) {
            ArrayList(((hour + 1)..23).toList().map{String.format("%02d", it)})
        } else {
            ArrayList((hour..23).toList().map{String.format("%02d", it)})
        }
        startAdapter.clear()
        startAdapter.addAll(hours)
        endAdapter.clear()
        endAdapter.addAll(hours)
        view?.findViewById<Spinner>(R.id.spinner_start_hours)?.setSelection(0)
        view?.findViewById<Spinner>(R.id.spinner_end_hours)?.setSelection(0)
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
        if (selectedStartHour > selectedEndHour || ((selectedStartHour == selectedEndHour) and (selectedStartMinute > selectedEndMinute))) {
            Toast.makeText(context, "Время начала должно быть раньше времени окончания!", Toast.LENGTH_LONG).show()
            return
        }
        val date = "2022-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d",selectedDay)
        val startTime = selectedStartHour.toString() + ":" + selectedStartMinute
        val endTime = selectedEndHour.toString() + ":" + selectedEndMinute
        val freeSeats = personNumber
        val level = 1
        val new_event = EventRequest(selectedSport, date, startTime, endTime, personNumber, freeSeats, level, selectedLatitude, selectedLongitude, selectedAddress)

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
                }
            })
        }.start()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val geocoder: Geocoder = Geocoder(context, Locale.getDefault())

        val nn = LatLng(56.326797,44.006516)
        marker = mMap.addMarker(MarkerOptions().position(nn).draggable(true))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nn, 12F))

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)

        mMap.setOnMarkerDragListener(object: GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {}

            override fun onMarkerDragEnd(marker: Marker) {
                val position = marker.getPosition()
                selectedLatitude = position.latitude
                selectedLongitude = position.longitude
                val addr = geocoder.getFromLocation(selectedLatitude, selectedLongitude, 1).get(0).getAddressLine(0).split(",")
                selectedAddress = addr[0]+ ", " + addr[1]
            }

            override fun onMarkerDragStart(p0: Marker) {}
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is FragmentCommunicator)
            communicator = parentFragment as FragmentCommunicator
    }

    override fun onDetach() {
        super.onDetach()
        communicator.fragmentDetached()
    }

    private fun CloseFragment() {
        val fm: FragmentManager = parentFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.remove(this)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        ft.commit()
    }

    interface FragmentCommunicator {
        fun fragmentDetached()
    }
}
