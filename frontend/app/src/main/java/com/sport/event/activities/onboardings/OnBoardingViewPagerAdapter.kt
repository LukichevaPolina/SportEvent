package com.sport.event.activities.onboardings

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class OnBoardingViewPagerAdapter(fm : FragmentManager) : FragmentStatePagerAdapter(fm) {

    var fragments : MutableList<Fragment> = ArrayList()

    fun addFragments(fragments : MutableList<Fragment>) {
        this.fragments = fragments
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size }

}