package com.sport.event.retrofit

import android.os.Bundle
import androidx.annotation.NonNull

interface RestClientCallbacks {
    fun onSuccess(@NonNull value: Bundle?)
    fun onFailure(@NonNull code: Int?)
    fun onError(@NonNull throwable: Throwable?)
}