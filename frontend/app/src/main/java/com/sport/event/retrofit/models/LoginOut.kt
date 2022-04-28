package com.sport.event.retrofit.models

class LoginOut (
        private val email: String,
        private val username: String,
        private val tokens: Tokens
) {
    fun getTokens(): Tokens? {
        return tokens
    }
}