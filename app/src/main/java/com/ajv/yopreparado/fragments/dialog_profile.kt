package com.ajv.yopreparado.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ajv.yopreparado.LoginActivity
import com.ajv.yopreparado.MainActivity
import com.ajv.yopreparado.ProfileActitivity
import com.ajv.yopreparado.R
import com.ajv.yopreparado.helper.sharedHelper
import com.ajv.yopreparado.services.utils
import com.bumptech.glide.Glide


class dialog_profile : DialogFragment() {

    private lateinit var viewSettings : View
    private lateinit var imgClose : ImageView
    private lateinit var imgLogout : ImageView
    private lateinit var lnEmergencyContact : LinearLayout
    private lateinit var lnMunicipality : LinearLayout
    private lateinit var tvInitial : TextView
    private lateinit var tvFullName : TextView
    private lateinit var tvEmail : TextView
    private lateinit var lnContingencyPlan : LinearLayout
    private lateinit var lnVideos : LinearLayout
    private lateinit var crdChangePassword : CardView
    private lateinit var crdProfileChange : CardView
    private lateinit var imgProfile : ImageView
    private lateinit var lnDRRM : LinearLayout
    private lateinit var lnNotification : LinearLayout
    private lateinit var lnVideoList : LinearLayout
    private lateinit var lnAboutUs : LinearLayout
    private lateinit var lnFAQ : LinearLayout
    private lateinit var lnFiles : LinearLayout

    val utils = utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewSettings = inflater.inflate(R.layout.fragment_profile_menu, container, false)
        viewSettings.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_dialog)

        imgClose = viewSettings.findViewById(R.id.imgClose)
        imgLogout = viewSettings.findViewById(R.id.imgLogout)
        lnEmergencyContact = viewSettings.findViewById(R.id.lnEmergencyContact)
        lnMunicipality = viewSettings.findViewById(R.id.lnMunicipality)
        tvInitial = viewSettings.findViewById(R.id.tvInitial)
        tvFullName = viewSettings.findViewById(R.id.tvFullName)
        tvEmail = viewSettings.findViewById(R.id.tvEmail)
        crdChangePassword = viewSettings.findViewById(R.id.crdChangePassword)
        lnContingencyPlan = viewSettings.findViewById(R.id.lnContingencyPlan)
        lnVideos = viewSettings.findViewById(R.id.lnVideos)
        crdProfileChange = viewSettings.findViewById(R.id.crdProfileChange)
        imgProfile = viewSettings.findViewById(R.id.imgProfile)
        lnDRRM = viewSettings.findViewById(R.id.lnDRRM)
        lnNotification = viewSettings.findViewById(R.id.lnNotification)
        lnVideoList = viewSettings.findViewById(R.id.lnVideoList)
        lnAboutUs = viewSettings.findViewById(R.id.lnAboutUs)
        lnFAQ = viewSettings.findViewById(R.id.lnFAQ)
        lnFiles = viewSettings.findViewById(R.id.lnFiles)

        imgClose.setOnClickListener {
            dismiss()
        }

        imgLogout.setOnClickListener {
            dismiss()
            activity?.let{
                val intent = Intent (it, LoginActivity::class.java)
                it.startActivity(intent)
            }
            activity?.finish()
        }

        lnEmergencyContact.setOnClickListener {
            val fragMan : FragmentManager = requireActivity().supportFragmentManager
            val hotlineListDialog = dialog_hotline()

            dismiss()
            hotlineListDialog.show(fragMan,"HotlineFragment")
        }

        lnMunicipality.setOnClickListener {
            val fragMan : FragmentManager = requireActivity().supportFragmentManager
            val municipalityListDialog = dialog_municipality()

            dismiss()
            municipalityListDialog.show(fragMan,"MunicipalityFragment")
        }

        crdChangePassword.setOnClickListener {
            val intent = Intent(activity, ProfileActitivity::class.java)
            intent.putExtra("fragment", "change_pass")
            startActivity(intent)
        }

        crdProfileChange.setOnClickListener {
            val intent = Intent(activity, ProfileActitivity::class.java)
            intent.putExtra("fragment", "profile_pic")
            startActivity(intent)
        }

        lnContingencyPlan.setOnClickListener {
            val intent = Intent(activity, ProfileActitivity::class.java)
            intent.putExtra("fragment", "contingency")
            startActivity(intent)
        }

        lnDRRM.setOnClickListener {
            val intent = Intent(activity, ProfileActitivity::class.java)
            intent.putExtra("fragment", "drrm")
            startActivity(intent)
        }

        lnVideos.setOnClickListener {
            val intent = Intent(activity, ProfileActitivity::class.java)
            intent.putExtra("fragment", "videos")
            startActivity(intent)
        }

        lnNotification.setOnClickListener {
            val intent = Intent(activity, ProfileActitivity::class.java)
            intent.putExtra("fragment", "notification")
            startActivity(intent)
        }

        lnVideoList.setOnClickListener {
            val intent = Intent(activity, ProfileActitivity::class.java)
            intent.putExtra("fragment", "video_list")
            startActivity(intent)
        }

        lnAboutUs.setOnClickListener {
            val intent = Intent(activity, ProfileActitivity::class.java)
            intent.putExtra("fragment", "about_us")
            startActivity(intent)
        }

        lnFAQ.setOnClickListener {
            val intent = Intent(activity, ProfileActitivity::class.java)
            intent.putExtra("fragment", "faq")
            startActivity(intent)
        }

        lnFiles.setOnClickListener {
            val intent = Intent(activity, ProfileActitivity::class.java)
            intent.putExtra("fragment", "files")
            startActivity(intent)
        }

        tvInitial.text = sharedHelper.getString("first_name").substring(0,1).uppercase()
        tvEmail.text = sharedHelper.getString("email_address")
        tvFullName.text = "${sharedHelper.getString("last_name")}, ${sharedHelper.getString("first_name")} ${sharedHelper.getString("middle_name")}"
        return viewSettings
    }

    override fun onResume() {

        val imageLink : String = sharedHelper.getString("image_link")

        if (imageLink.equals("-")) {
            tvInitial.visibility = VISIBLE
            imgProfile.visibility = GONE
        } else {
            tvInitial.visibility = GONE
            imgProfile.visibility = VISIBLE

            Glide.with(this)
                .load(Uri.parse(imageLink))
                .into(imgProfile)
        }

        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}