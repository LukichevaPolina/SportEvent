package com.sport.event.retrofit

import androidx.annotation.NonNull

interface RestClientCallbacks {
    fun onSuccess(@NonNull value: String?)
    fun onFailure(@NonNull code: Int?)
    fun onError(@NonNull throwable: Throwable?)
}