package com.sport.event.retrofit

import com.google.gson.GsonBuilder
import com.sport.event.retrofit.models.LoginOut
import com.sport.event.retrofit.models.LoginPut
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RestClient {

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.13:8000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        service = retrofit.create(IUserApi::class.java)
    }

    companion object {
        lateinit var service: IUserApi
        private var restClient: RestClient? = null
        val instance: RestClient?
            get() {
                if (restClient == null) {
                    restClient = RestClient()
                }
                return restClient
            }
    }

    fun login(email: String, password: String, callbacks: LoginCallbacks) {
        val userLogin = LoginPut(email, password)
//--------------------------------------Async executing--------------------------------------
        service.loginUser(userLogin)?.enqueue(object : Callback<LoginOut?> {
            override fun onResponse(call: Call<LoginOut?>, response: Response<LoginOut?>) {
                val tokensJson: LoginOut? = response.body()
                if (response.isSuccessful && tokensJson != null) {
                    val authToken: String? = tokensJson?.getTokens()?.getAccessToken()
                    callbacks.onSuccess(authToken);
                } else {
                    println("Response is %s" + response.code())
                }
            }
            override fun onFailure(call: Call<LoginOut?>, t: Throwable) {
                callbacks.onError(t)
            }
        })
//-------------------------------------------------------------------------------------------
    }
}

