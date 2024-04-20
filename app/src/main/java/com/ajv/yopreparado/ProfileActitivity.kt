package com.ajv.yopreparado

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ajv.yopreparado.fragments.aboutus
import com.ajv.yopreparado.fragments.calamity_menu
import com.ajv.yopreparado.fragments.change_password
import com.ajv.yopreparado.fragments.change_profile
import com.ajv.yopreparado.fragments.document_viewer
import com.ajv.yopreparado.fragments.faq
import com.ajv.yopreparado.fragments.file_view
import com.ajv.yopreparado.fragments.notification_list
import com.ajv.yopreparado.fragments.video_details
import com.ajv.yopreparado.fragments.video_list
import com.ajv.yopreparado.helper.sharedHelper
import com.ajv.yopreparado.services.utils

class ProfileActitivity : AppCompatActivity() {
    private lateinit var profileFragMan: FragmentManager
    private lateinit var profileFragTrans: FragmentTransaction

    var fragToOpen : String? = ""
    interface IOnBackPressed {
        fun onBackPressed(): Boolean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_actitivity)

        fragToOpen = intent.getStringExtra("fragment")
        profileFragMan = supportFragmentManager
        profileFragTrans = profileFragMan.beginTransaction()

        if (fragToOpen.equals("change_pass")) {
            val changePasswordFrag = change_password(this)
            profileFragTrans.replace(R.id.frProfile, changePasswordFrag)
        } else if (fragToOpen.equals("profile_pic")) {
            val changeProfileFrag = change_profile(this)
            profileFragTrans.replace(R.id.frProfile, changeProfileFrag)
        } else if (fragToOpen.equals("contingency")) {
            val documentFrag = document_viewer(this)
            sharedHelper.putString("docu","contingency")
            profileFragTrans.replace(R.id.frProfile,documentFrag)
        } else if (fragToOpen.equals("drrm")) {
            val documentFrag = document_viewer(this)
            sharedHelper.putString("docu","drrm")
            profileFragTrans.replace(R.id.frProfile,documentFrag)
        } else if (fragToOpen.equals("notification")) {
            val notifFrag = notification_list(this)
            profileFragTrans.replace(R.id.frProfile,notifFrag)
        } else if (fragToOpen.equals("videos")) {
            val videoFrag = calamity_menu(this)
            profileFragTrans.replace(R.id.frProfile,videoFrag)
        } else if (fragToOpen.equals("video_list")) {
            val videoFragList = video_list(this)
            profileFragTrans.replace(R.id.frProfile,videoFragList)
        } else if (fragToOpen.equals("about_us")) {
            val aboutFrag = aboutus(this)
            profileFragTrans.replace(R.id.frProfile,aboutFrag)
        } else if (fragToOpen.equals("faq")) {
            val faqFrag = faq(this)
            profileFragTrans.replace(R.id.frProfile,faqFrag)
        } else if (fragToOpen.equals("files")) {
            val filesFrag = file_view(this)
            profileFragTrans.replace(R.id.frProfile,filesFrag)
        }

        profileFragTrans.addToBackStack(fragToOpen)
        profileFragTrans.commit()
    }

    fun gotoVideoDetails(eventID: Int) {
        val videoPlayer = Intent(this, VideoPlayer::class.java)
        videoPlayer.putExtra("eventID", eventID)
        startActivity(videoPlayer)
    }

    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.frProfile)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
            goBack()
        }
    }

    fun goBack() {
        finish()
    }

}