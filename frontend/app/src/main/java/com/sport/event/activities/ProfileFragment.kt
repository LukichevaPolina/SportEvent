package com.sport.event.activities

import android.accounts.AccountManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.sport.event.Constants
import com.sport.event.R
import com.sport.event.accountManager.AccountManagerHelper

class ProfileFragment : Fragment() {

    private lateinit var editBtn: ImageButton
    private lateinit var settingsBtn: ImageButton
    private var editProfileFragment: EditProfileFragment = EditProfileFragment()
    private var settingsProfileFragment: SettingsProfileFragment = SettingsProfileFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        editBtn = view.findViewById(R.id.icon_edit)
        settingsBtn = view.findViewById(R.id.icon_settings)

        val accountManager: AccountManager = AccountManager.get(context)
        val account = AccountManagerHelper().getAccount(accountManager)

        val fragment_trans = getFragmentManager()?.beginTransaction()

        val name: String = accountManager.getUserData(account, Constants.NAME)
        val surname: String = accountManager.getUserData(account, Constants.SURNAME)
        val username: String = accountManager.getUserData(account, Constants.USERNAME)

        val surnameTextView: TextView = view.findViewById(R.id.user_surname)
        val nameTextView: TextView = view.findViewById(R.id.user_name)
        val usernameTextView: TextView = view.findViewById(R.id.user_username)

        surnameTextView.text = surname
        nameTextView.text = name
        usernameTextView.text = username

        editBtn.setOnClickListener {
            fragment_trans?.replace(R.id.container, editProfileFragment)?.commit()
        }

        settingsBtn.setOnClickListener {
            fragment_trans?.replace(R.id.container, settingsProfileFragment)?.commit()
        }

        return view
    }

}
