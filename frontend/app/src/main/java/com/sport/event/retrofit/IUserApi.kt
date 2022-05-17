package com.sport.event.retrofit

import com.sport.event.retrofit.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface IUserApi {
    @POST("auth/register/")
    fun registerUser(@Body user: UserRegistrationRequest?): Call<User?>?

    @POST("auth/login/")
    fun loginUser(@Body loginRequest: LoginRequest?): Call<LoginResponse?>?

    @POST("auth/token/refresh/")
    suspend fun refresh(@Body refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse

    @POST("events/")
    fun addEvent(
        @Header("Authorization") token: String,
        @Body eventRequest: EventRequest
    ): Call<Event>?

    @GET("events/")
    fun getEvents(@Header("Authorization") token: String?): Call<ArrayList<Event>>?

    @GET("events/{id}")
    fun getEvent(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<ArrayList<Event>>?

    @GET("events/schedule/")
    fun getSchedule(@Header("Authorization") token: String): Call<ArrayList<Event>>?

    @PATCH("events/{id}/join/")
    fun join(@Header("Authorization") token: String, @Path("id") id: Int?) : Call<Event>?

    @PATCH("events/{id}/unjoin/")
    fun unjoin(@Header("Authorization") token: String, @Path("id") id: Int?) : Call<Event>?

    @GET("events/date/")
    fun getEventsDate(@Header("Authorization") token: String, @Query("date") date: String?): Call<ArrayList<Event>>

    @GET("events/after_date/")
    fun eventsAfterDate(@Header("Authorization") token: String, @Query("date") date: String?): Call<ArrayList<Event>>

    @GET("events/visited/")
    fun eventsVisited(@Header("Authorization") token: String): Call<ArrayList<Event>>

    @GET("events/created/")
    fun eventsCreated(@Header("Authorization") token: String): Call<ArrayList<Event>>

//    @GET("events/filters/")
//    fun eventsFilters(@Header("Authorization") token: String): Call<ArrayList<Event>>
}