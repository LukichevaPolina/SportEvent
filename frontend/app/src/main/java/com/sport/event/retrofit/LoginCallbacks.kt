package com.sport.event.retrofit

import android.content.Intent
import androidx.annotation.NonNull

interface LoginCallbacks {
    fun onSuccess(@NonNull value: String?)
    fun onError(@NonNull throwable: Throwable?)
}