package com.sport.event.retrofit

import android.app.Application

class APIApp : Application() {
    override fun onCreate() {
        super.onCreate()
        restClient = RestClient.instance
    }

    companion object {
        var restClient: RestClient? = null
    }
}
