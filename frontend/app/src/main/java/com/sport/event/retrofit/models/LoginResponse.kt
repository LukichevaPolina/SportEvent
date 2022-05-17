package com.sport.event.retrofit.models

class LoginResponse (
        private val id: Int,
        private val email: String,
        private val username: String,
        private val tokens: Tokens
) {
    fun getTokens(): Tokens? {
        return tokens
    }

    fun getId(): Int {
        return id
    }

    fun getUsername(): String {
        return username
    }
}