package com.sport.event.retrofit.models

class EventRequest(
    private val sport: Int,
    private val date: String?,
    private val start_time: String?,
    private val end_time: String?,
    private val person_number: Int,
    private val free_seats: Int?,
    private val level: Int,
    private val latitude: Double,
    private val longitude: Double,
    private val address: String
)