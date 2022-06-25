package com.sport.event.dataHandlers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GroundsModelFactory  : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GroundViewModel::class.java)) {
            GroundViewModel() as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}