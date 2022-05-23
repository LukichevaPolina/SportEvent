package com.sport.event.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient {

    var service: IUserApi

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
<<<<<<< HEAD
            .baseUrl("http://192.168.151.57:8000/")
=======
            .baseUrl("http://192.168.0.13:8000/")
>>>>>>> c791567751014e114abd21481b0ac46c30ab8ee1
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        service = retrofit.create(IUserApi::class.java)
    }

    companion object {
        private var restClient: RestClient? = null
        val instance: RestClient?
            get() {
                if (restClient == null) {
                    restClient = RestClient()
                }
                return restClient
            }
    }
}

