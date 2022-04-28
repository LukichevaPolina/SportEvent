package com.sport.event.retrofit.models

class Tokens(
    private val refresh: String,
    private val access: String
) {
    fun getAccessToken(): String {
        return access
    }

    fun getRefreshToken(): String {
        return refresh
    }
}