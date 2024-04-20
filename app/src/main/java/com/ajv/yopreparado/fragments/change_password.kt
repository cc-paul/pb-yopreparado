package com.ajv.yopreparado.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.ajv.yopreparado.ProfileActitivity
import com.ajv.yopreparado.R
import com.ajv.yopreparado.data.loginData
import com.ajv.yopreparado.data.passwordData
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils

class change_password(private val profileContext : ProfileActitivity) : Fragment(),
    ProfileActitivity.IOnBackPressed {

    private lateinit var changePasswordView : View
    private lateinit var lnChangePassword : LinearLayout
    private lateinit var etNewPassword : EditText
    private lateinit var etConfirmPassword : EditText
    private lateinit var layoutChangePassword : LinearLayout
    private lateinit var imgBack : ImageView

    private val changePassAPIService = restApiService()
    private val utils = utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changePasswordView = inflater.inflate(R.layout.fragment_change_password, container, false)

        lnChangePassword = changePasswordView.findViewById(R.id.lnChangePassword)
        etNewPassword = changePasswordView.findViewById(R.id.etNewPassword)
        etConfirmPassword = changePasswordView.findViewById(R.id.etConfirmPassword)
        layoutChangePassword = changePasswordView.findViewById(R.id.layoutChangePassword)
        imgBack = changePasswordView.findViewById(R.id.imgBack)

        lnChangePassword.setOnClickListener {
            if (!utils.hasInternet(this.requireContext())) {
                utils.showSnackMessage(layoutChangePassword,"Please check your internet connection")
                return@setOnClickListener
            }

            utils.showProgress(this.requireContext())

            val changePassInfo = passwordData(
                mode = "change_password",
                new_password = etNewPassword.text.toString(),
                confirm_password = etConfirmPassword.text.toString()
            )

            changePassAPIService.changePassword(changePassInfo) { it ->
                utils.closeProgress()
                utils.showSnackMessage(layoutChangePassword,it?.messages?.get(0).toString())

                if (!it!!.success) {

                    var errorMessage = if (it.data != null) {
                        it.data.joinToString("\n")
                    } else {
                        it?.messages?.get(0).toString()
                    }

                    utils.showSnackMessage(changePasswordView,errorMessage)
                } else {
                    utils.showToastMessage(requireContext().applicationContext, it?.messages?.get(0).toString())
                    imgBack.performClick()
                }
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                imgBack.performClick()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        imgBack.setOnClickListener {
            profileContext.goBack()
        }

        return changePasswordView
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}
