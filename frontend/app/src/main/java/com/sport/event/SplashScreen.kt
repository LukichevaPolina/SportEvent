package com.sport.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import java.util.concurrent.TimeUnit

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SportEvent)
        super.onCreate(savedInstanceState)
        val intent = Intent(this, Onbording1::class.java)
        startActivity(intent)
        finish()

        /*val user = UserDb.getCurrentUser()
        routeToAppropriatePage(user)
        finish()*/
    }

    /*private fun routeToAppropriatePage(user: User?) {
        when {
            user == null -> OnboardingActivity.start(this)
            user.hasPhoneNumber() -> EditProfileActivity.start(this)
            user.hasSubscriptionExpired() -> PaymentPlansActivity.start(this)
            else -> HomeActivity.start(this)
        }
    }*/
}