package com.sport.event.retrofit.models

class RefreshTokenResponse (
    private val access: String?
)
{
    fun getAccessToken(): String? {
        return access
    }
}