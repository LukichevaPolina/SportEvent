package com.sport.event.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.sport.event.R

class SettingsProfileFragment : Fragment() {

    private lateinit var backBtn: ImageButton
    private var profileFragment: ProfileFragment = ProfileFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_settings, container, false)
        backBtn = view.findViewById(R.id.icon_back_settings)

        val fragment_trans = getFragmentManager()?.beginTransaction()

        backBtn.setOnClickListener {
            fragment_trans?.replace(R.id.container, profileFragment)?.commit()
        }

        return view
    }

}