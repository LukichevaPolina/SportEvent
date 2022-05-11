package com.sport.event.activities.eventsFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EventsModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EventsViewModel::class.java)) {
            EventsViewModel() as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}