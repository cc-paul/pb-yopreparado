package com.ajv.yopreparado.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import com.ajv.yopreparado.ProfileActitivity
import com.ajv.yopreparado.R


class aboutus(private val profileContext : ProfileActitivity) : Fragment() , ProfileActitivity.IOnBackPressed {
    private lateinit var aboutUsView : View
    private lateinit var imgBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        aboutUsView = inflater.inflate(R.layout.fragment_about_us, container, false)
        imgBack = aboutUsView.findViewById(R.id.imgBack)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                imgBack.performClick()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        imgBack.setOnClickListener {
            profileContext.goBack()
        }

        return aboutUsView
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}