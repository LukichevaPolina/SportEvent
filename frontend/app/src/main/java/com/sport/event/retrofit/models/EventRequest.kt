package com.sport.event.retrofit.models

class EventRequest(
    private val sport: Int,
    private val date: String?,
    private val strat_time: String?,
    private val end_time: String?,
    private val person_number: Int,
    private val free_seats: Int?,
    private val level: Int,
    private val latitude: Int,
    private val longitude: Int,
    private val members: ArrayList<String?>
)