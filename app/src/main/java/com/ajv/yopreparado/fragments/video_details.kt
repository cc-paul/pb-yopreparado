package com.ajv.yopreparado.fragments

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ajv.yopreparado.R


class video_details : Fragment() {
    private lateinit var videoView : View
    var rotated = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        videoView = inflater.inflate(R.layout.fragment_video_details, container, false)



        return videoView
    }

    fun rotate() {
        val currentOrientation = activity?.resources?.configuration?.orientation //this is diffrent from  Configuration.ORIENTATION_LANDSCAPE
        Log.e("currentOrientation--->",currentOrientation.toString())

        if (currentOrientation != null) {
            if (currentOrientation !=  Configuration.ORIENTATION_LANDSCAPE) {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }else{
                rotated=true
            }
        }else{
            //impossible
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (rotated)
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onStart() {
        super.onStart()
        rotate()
    }
}