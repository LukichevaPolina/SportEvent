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
            .baseUrl("http://192.168.0.14:8000/")
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

//    fun refreshToken(refreshToken: String?, callbacks: RestClientCallbacks) {
//        val refreshTokenRequest = RefreshTokenRequest(refreshToken)
//        service.refresh(refreshTokenRequest)?.enqueue(object: Callback<RefreshTokenResponse?> {
//            override fun onResponse(
//                call: Call<RefreshTokenResponse?>,
//                response: Response<RefreshTokenResponse?>
//            ) {
//                val tokenJson: RefreshTokenResponse? = response.body()
//                if (response.isSuccessful && tokenJson != null) {
//                    val data = Bundle()
//                    data.putString(Constants.AUTH_TOKEN, tokenJson.getAccessToken())
//                    callbacks.onSuccess(data)
//                } else {
//                    callbacks.onFailure(response.code())
//                }
//            }
//
//            override fun onFailure(call: Call<RefreshTokenResponse?>, t: Throwable) {
//                callbacks.onError(t)
//            }
//        })
//    }
}

