package com.sport.event.activities.onboardings

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sport.event.R


class OnBoardingActivity : AppCompatActivity() {

    private var currentPage = 0
    private var Onbording1Fragment: Onbording1Fragment = Onbording1Fragment()
    private var Onbording2Fragment: Onbording2Fragment = Onbording2Fragment()
    private var Onbording3Fragment: Onbording3Fragment = Onbording3Fragment()
    private var fragments: MutableList<Fragment> = ArrayList()
    private lateinit var buttonNext: Button
    private lateinit var buttonSkip: Button


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onbordings)
            supportFragmentManager.beginTransaction().replace(R.id.container, Onbording1Fragment).commit()
    }
}
