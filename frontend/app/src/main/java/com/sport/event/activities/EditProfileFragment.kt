package com.sport.event.activities

import android.accounts.AccountManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sport.event.Constants
import com.sport.event.R
import com.sport.event.accountManager.AccountManagerHelper

class EditProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        val accountManager: AccountManager = AccountManager.get(context)
        val account = AccountManagerHelper().getAccount(accountManager)

        val name: String = accountManager.getUserData(account, Constants.NAME)
        val surname: String = accountManager.getUserData(account, Constants.SURNAME)
        val username: String = accountManager.getUserData(account, Constants.USERNAME)
        val birthday: String = accountManager.getUserData(account, Constants.BIRTHDAY)
        val email: String = accountManager.getUserData(account, Constants.EMAIL)
        val country: String = accountManager.getUserData(account, Constants.COUNTRY)
        val locality: String = accountManager.getUserData(account, Constants.LOCALITY)

        val surnameTextView: TextView = view.findViewById(R.id.surname)
        val nameTextView: TextView = view.findViewById(R.id.name)
        val usernameTextView: TextView = view.findViewById(R.id.username)
        val birthdayTextView: TextView = view.findViewById(R.id.birthday)
        val emailTextView: TextView = view.findViewById(R.id.email)
        val countryTextView: TextView = view.findViewById(R.id.country)
        val localityTextView: TextView = view.findViewById(R.id.locality)

        val fragment_trans = getFragmentManager()?.beginTransaction()

        surnameTextView.text = surname
        nameTextView.text = name
        usernameTextView.text = username
        birthdayTextView.text = birthday
        emailTextView.text = email
        countryTextView.text = country
        localityTextView.text = locality

        val back: ImageButton = view.findViewById(R.id.icon_back_)
        back.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.container, ProfileFragment(), "EDIT_PROFILE_TAG").commit()
        }

        val tick: ImageButton = view.findViewById(R.id.icon_tick)
        back.setOnClickListener {
            // TO DO: Update data in the DB
            childFragmentManager.beginTransaction().replace(R.id.container, ProfileFragment(), "EDIT_PROFILE_TAG").commit()
        }

        return view
    }
}