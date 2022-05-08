package com.sport.event.retrofit.models

class User(
    private val email: String?,
    private val username: String?,
    private val password: String?,
    private val name: String?,
    private val surname: String?,
    private val birthday: String?,
    private val country: String?,
    private val locality: String?,
    private val favorite_sports: ArrayList<Int>?
)