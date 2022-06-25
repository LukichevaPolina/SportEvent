package com.sport.event.dataHandlers

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sport.event.R
import android.graphics.drawable.Drawable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.core.view.drawToBitmap
import android.os.Build

import android.view.ViewTreeObserver.OnGlobalLayoutListener

import android.view.ViewTreeObserver





class MarkerHelper (val map: GoogleMap?, context: Context?) {

    val eventsMarkers: ArrayList<Marker?> = arrayListOf()

    fun createEventMarker(view: View?, latitude: Double, longitude: Double, _sport: String, startTime: String, endTime: String) {

        val sport_len = R.string.sport.toString().length
        var sport = _sport
        if (sport.length > sport_len) {
            sport = sport.substring(0, sport_len - 3) + "..."
        }
        view?.findViewById<TextView>(R.id.first_line)?.text = sport

        val timeText = "$startTime - $endTime"
        view?.findViewById<TextView>(R.id.second_line)?.text = timeText
        view?.findViewById<ImageView>(R.id.marker)?.setImageResource(R.drawable.ic_eventmarker)
        val layout: FrameLayout? = view?.findViewById(R.id.event_marker)

        createMarker(layout, latitude, longitude, sport, timeText, true)

    }

    fun createGroundMarker(view: View?, latitude: Double, longitude: Double, _name: String) {
        val name_len = R.string.sport.toString().length
        var name = _name
        if (name.length > name_len) {
            name = name.substring(0, name_len - 3) + "..."
        }
        view?.findViewById<TextView>(R.id.first_line)?.text = name
        view?.findViewById<TextView>(R.id.second_line)?.text = ""
        view?.findViewById<ImageView>(R.id.marker)?.setImageResource(R.drawable.ic_groundmarker)

        val layout: FrameLayout? = view?.findViewById(R.id.event_marker)

        createMarker(layout, latitude, longitude, name, "")
    }

    fun clearEventMarkers() {
        for (marker in eventsMarkers) {
            marker?.remove()
        }
        eventsMarkers.clear()
    }

    private fun createMarker(view: View?, latitude: Double, longitude: Double, firstLine: String, secondLine: String, is_event: Boolean = false) {

        val vto = view?.viewTreeObserver
        vto?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.findViewById<TextView>(R.id.first_line).text = firstLine
                view.findViewById<TextView>(R.id.second_line)?.text = secondLine

                if (!is_event) { view.findViewById<ImageView>(R.id.marker).setImageResource(R.drawable.ic_groundmarker)}
                else {
                    view.findViewById<ImageView>(R.id.marker)?.setImageResource(R.drawable.ic_eventmarker)
                }
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val bm = view.drawToBitmap()

                val marker = map?.addMarker(
                    MarkerOptions()
                        .position(LatLng(latitude, longitude))
                        .icon( BitmapDescriptorFactory.fromBitmap(bm) )
                )
                if (is_event) { eventsMarkers.add(marker)}
            }
        })


    }

}