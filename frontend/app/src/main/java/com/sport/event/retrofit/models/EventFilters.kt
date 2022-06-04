package com.sport.event.retrofit.models

class EventFilters (
    var sport: Int?,
    var date: String?,
    var start_time: String?,
    var free_seats_gte: Int,
    var free_seats_lte: Int)