package com.sport.event.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.sport.event.R
import com.sport.event.activities.eventsFragment.EventsFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    private var eventsFragment: EventsFragment = EventsFragment()
    private var mapFragment: MapFragment = MapFragment()
    private var scheduleFragment: ScheduleFragment = ScheduleFragment()
    private var profileFragment: ProfileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //bottom navigation bar
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setSelectedItemId(R.id.map);
        supportFragmentManager.beginTransaction().replace(R.id.container,mapFragment).commit()
        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.events -> {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, eventsFragment).commit()
                    return@OnItemSelectedListener true
                }
                R.id.map -> {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, mapFragment).commit()
                    return@OnItemSelectedListener true
                }
                R.id.schedule -> {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, scheduleFragment).commit()
                    return@OnItemSelectedListener true
                }
                R.id.profile -> {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, profileFragment).commit()
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }
}


