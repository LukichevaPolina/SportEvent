package com.sport.event.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.Constants
import com.sport.event.R
import com.sport.event.retrofit.APIApp
import com.sport.event.retrofit.models.User
import com.sport.event.retrofit.models.UserRegistrationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationScreen3 : AppCompatActivity() {

    lateinit var button: Button
    private lateinit var buttonBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration3)
        findViewById<View>(R.id.loadingPanel).visibility = View.GONE
        button = findViewById(R.id.btnRegistry)
        button.setOnClickListener {
            registration()
        }
        buttonBack = findViewById(R.id.icon_back)
        buttonBack.setOnClickListener {
            val intent = Intent(this, RegistrationScreen2::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun registration(){
        findViewById<View>(R.id.loadingPanel).visibility = View.VISIBLE

        //get user data for registration
        val name = intent.getStringExtra(Constants.NAME)
        val surname = intent.getStringExtra(Constants.SURNAME)
        val birthday = intent.getStringExtra(Constants.BIRTHDAY)
        val email = intent.getStringExtra(Constants.EMAIL)
        val country = intent.getStringExtra(Constants.COUNTRY)
        val locality = intent.getStringExtra(Constants.LOCALITY)
        val username = (findViewById<View>(R.id.username) as TextView).text.toString()
        val favoriteSports = (findViewById<View>(R.id.favorite_sports) as TextView).text.toString()
        val password = (findViewById<View>(R.id.password) as TextView).text.toString()
        val repeatPassword = (findViewById<View>(R.id.repeat_password) as TextView).text.toString()

        val arrayFavoriteSports = arrayListOf(favoriteSports.toInt())

        //check passwords matching
        if (password != repeatPassword) {
            Toast.makeText(applicationContext, "Passwords don't match!", Toast.LENGTH_LONG).show()
            return
        }
        val userRegistration = UserRegistrationRequest(email, username, password, name, surname, birthday, country, locality, arrayFavoriteSports)

        //send request to server for register user
        APIApp.restClient?.service?.registerUser(userRegistration)?.enqueue(object :
            Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                val user: User? = response.body()
                if (response.isSuccessful && user != null) {
                    startCheckEmailActivity()
                } else {
                    when (response.code()) {
                    400, 401 -> Toast.makeText(applicationContext, "Incorrect data", Toast.LENGTH_LONG).show()
                    else -> {
                        Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }
                findViewById<View>(R.id.loadingPanel).visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
            override fun onFailure(call: Call<User?>, t: Throwable) {
                Toast.makeText(applicationContext, "Connection to server failed, try again later", Toast.LENGTH_LONG).show()
                findViewById<View>(R.id.loadingPanel).visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })
    }

    fun startCheckEmailActivity() {
        val intent = Intent(this, CheckEmailScreen::class.java)
        startActivity(intent)
        findViewById<View>(R.id.loadingPanel).visibility = View.GONE
        finish()
    }
}