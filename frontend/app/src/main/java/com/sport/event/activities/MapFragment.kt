package com.sport.event.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sport.event.R
import com.sport.event.dataHandlers.CalendarAdapter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback, CreateEventFragment.FragmentCommunicator {
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

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter

    private lateinit var mMap: GoogleMap

    private var address : EditText? = null
    private lateinit var button: ImageButton
    private var marker: Marker? = null


    //creates the view for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        date = sdf.format(cal.time)

        val view: View = inflater.inflate(R.layout.fragment_map, container, false)
        button = view.findViewById(R.id.icon_search)
        address = view.findViewById(R.id.address)

        calendarRecyclerView = view.findViewById(R.id.calendar_recycler_view)

        setUpCalendar()

        var latitude: Double
        var longitude: Double


        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        button.setOnClickListener {
            val geocoder = Geocoder(context, Locale.getDefault())
            val address1: String = address?.text.toString()
            var addresses: List<Address> = emptyList()
            try {
                addresses = geocoder.getFromLocationName(address1, 1)
                if (!addresses.isEmpty()) {
                    latitude = addresses[0].latitude;
                    longitude = addresses[0].longitude;
                    val latLng = LatLng(latitude, longitude)
                    marker?.remove()
                    marker = mMap.addMarker(MarkerOptions().position(latLng).title(address1))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
                } else {
                    marker?.remove()
                    Toast.makeText(
                        context,
                        "The address does not exist: ${address1.toString()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (ioException: IOException) {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            } catch (illegalArgumentException: IllegalArgumentException) {
                Toast.makeText(context, "Illegal argument", Toast.LENGTH_LONG).show()
            }
        }

        //plus button -> create new event fragment
        val plusButton: Button = view.findViewById(R.id.button_plus)
        plusButton.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.container, CreateEventFragment(), "CREATE_EVENT_TAG").commit()
        }


        //filter button -> filter fragment
        val filterButton: ImageButton = view.findViewById(R.id.filters_button)
        filterButton.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.container, FilterFragment(), "FILTER_TAG").commit()
        }

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val nn = LatLng(56.326797,44.006516)
        marker = mMap.addMarker(MarkerOptions().position(nn))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nn, 12F))

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    //method for creating new instances of the fragment, a factory method
    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
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
                println(selectedDayRequest)
//                viewModel.getEventsDate(selectedDayRequest, accountManager)
//                eventsRecyclerView.scrollToPosition(0)
            }
        })
    }

    override fun fragmentDetached() {
        println("Close fragment")
    }

    private fun my_location() {

    }

    private fun getLocationPermission() {
        /*if (this.context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            }
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }*/
    }
}
