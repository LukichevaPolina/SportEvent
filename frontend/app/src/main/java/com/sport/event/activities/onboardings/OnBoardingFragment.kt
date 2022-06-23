package com.sport.event.activities.onboardings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.sport.event.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.sport.event.activities.StartScreen
import kotlinx.android.synthetic.main.activity_onbording1.*



class OnBoardingFragment : Fragment() {

    interface OnBoardingListener {
        fun onNextClick()
        fun onSkipClick()
    }

    companion object {
        val NAME = "name"

        /**
         * @param name a string to be displayed on the fragment  * @param listener click listener to pass click events to the activity  */
        fun newInstance(name : String, listener : OnBoardingListener) : Fragment {
            val fragment = OnBoardingFragment()
            val bundle = Bundle()
            bundle.putString(NAME, name)
            fragment.arguments = bundle
                    fragment.onBoardingListener = listener
            return fragment
        }
    }

    private lateinit var onBoardingListener : OnBoardingListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.activity_onbording1, container, false)
        btnNext.setOnClickListener{
            onBoardingListener.onNextClick()
        }

        btnSkip.setOnClickListener{
            onBoardingListener.onSkipClick()
        }
        return view
    }

}