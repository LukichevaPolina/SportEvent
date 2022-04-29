package com.sport.event.retrofit

import com.sport.event.retrofit.models.LoginResponse
import com.sport.event.retrofit.models.LoginRequest
import com.sport.event.retrofit.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface IUserApi {
    @POST("auth/register/")
    fun registerUser(@Body user: User?): Call<User?>?

    @POST("auth/login/")
    fun loginUser(@Body loginRequest: LoginRequest?): Call<LoginResponse?>?
}