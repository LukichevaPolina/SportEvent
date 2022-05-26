package com.sport.event.retrofit.models

class Event (
    val id: Int,
    val owner: String,
    val sport: String,
    private val date: String,
    val start_time: String,
    val end_time: String,
    private val person_number: Int,
    val free_seats: Int,
    private val level: Int,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val members: ArrayList<Int>,
)
