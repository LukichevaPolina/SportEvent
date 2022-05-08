package com.sport.event.activities

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Marker
import com.sport.event.R
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var address : EditText? = null
    private lateinit var button: Button
    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        button = findViewById(R.id.button)
        address = findViewById(R.id.address)

        var latitude : Double
        var longitude : Double

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        button.setOnClickListener() {
            val geocoder = Geocoder(this, Locale.getDefault())
            val address1: String = address?.text.toString()
            var addresses: List<Address> = emptyList()
            try {
                addresses = geocoder.getFromLocationName(address1,  1)
                if (!addresses.isEmpty()) {
                    latitude = addresses[0].latitude;
                    longitude = addresses[0].longitude;
                    val latLng = LatLng(latitude, longitude)
                    marker?.remove()
                    marker = mMap.addMarker(MarkerOptions().position(latLng).title(address1))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
                } else {
                    marker?.remove()
                    Toast.makeText(this, "The address does not exist: ${address1.toString()}", Toast.LENGTH_LONG).show()
                }
            }
            catch (ioException: IOException) {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
            catch (illegalArgumentException: IllegalArgumentException) {
                Toast.makeText(this, "Illegal argument", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val nn = LatLng(56.326797,44.006516)
        marker = mMap.addMarker(MarkerOptions().position(nn))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nn, 12F))

        if (mMap != null)
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

}