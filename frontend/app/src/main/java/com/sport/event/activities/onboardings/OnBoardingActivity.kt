package com.sport.event.activities.onboardings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.sport.event.R
import kotlinx.android.synthetic.main.activity_onbordings.*
import androidx.viewpager.widget.PagerAdapter
import com.sport.event.activities.*


class OnBoardingActivity : AppCompatActivity() {

    private var currentPage = 0
    private var Onbording1: Onbording1 = Onbording1()
    private var Onbording2: Onbording2 = Onbording2()
    private var Onbording3: Onbording3 = Onbording3()
    private var fragments: MutableList<Fragment> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onbordings)
        supportFragmentManager.beginTransaction().replace(R.id.container,Onbording1).commit()
    }
}
