package com.sport.event.retrofit

import com.google.gson.GsonBuilder
import com.sport.event.retrofit.models.LoginResponse
import com.sport.event.retrofit.models.LoginRequest
import com.sport.event.retrofit.models.User
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
            .baseUrl("http://192.168.1.6:8000/")
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

    fun login(email: String, password: String, callbacks: RestClientCallbacks) {
        val userLogin = LoginRequest(email, password)
        service.loginUser(userLogin)?.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
                val tokensJson: LoginResponse? = response.body()
                if (response.isSuccessful && tokensJson != null) {
                    val authToken: String? = tokensJson.getTokens()?.getAccessToken()
                    callbacks.onSuccess(authToken)
                } else {
                    callbacks.onFailure(response.code())
                }
            }
            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                callbacks.onError(t)
            }
        })
    }

    fun register(name: String?,
                 surname: String?,
                 birthday: String?,
                 email: String?,
                 country: String?,
                 locality: String?,
                 username: String?,
                 favoriteSports: ArrayList<Int>?,
                 password: String?,
                 callbacks: RestClientCallbacks) {
        val user = User(email, username, password, name, surname, birthday, country, locality) //favoriteSports)
        service.registerUser(user)?.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                val tokensJson: User? = response.body()
                if (response.isSuccessful && tokensJson != null) {
                    callbacks.onSuccess(null)
                } else {
                    callbacks.onFailure(response.code())
                }
            }
            override fun onFailure(call: Call<User?>, t: Throwable) {
                callbacks.onError(t)
            }
        })
    }
}

