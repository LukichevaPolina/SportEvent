package com.sport.event.retrofit.models

class LoginResponse (
        val id: Int,
        val email: String,
        val username: String,
        val tokens: Tokens,
        val name: String,
        val surname: String
)