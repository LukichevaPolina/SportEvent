package com.sport.event.activities

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sport.event.R
import java.io.IOException
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var address : EditText? = null
    private lateinit var button: Button
    private var marker: Marker? = null

    //creates the view for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_map, container, false)
        button = view.findViewById(R.id.button)
        address = view.findViewById(R.id.address)

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
}