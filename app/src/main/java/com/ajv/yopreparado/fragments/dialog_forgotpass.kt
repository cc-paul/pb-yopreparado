package com.ajv.yopreparado.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.ajv.yopreparado.R
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils

class dialog_forgotpass : DialogFragment() {

    lateinit var etEmail: EditText
    lateinit var lnPasswordReset : LinearLayout
    lateinit var lnForgotPass : LinearLayout
    lateinit var lnCancel : LinearLayout

    val utils = utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val forgotPasswordView : View = inflater.inflate(R.layout.fragment_dialog_forgotpass, container, false)
        val resetPasswordAPI = restApiService()

        forgotPasswordView.apply {
            etEmail = findViewById(R.id.etEmail)
            lnPasswordReset = findViewById(R.id.lnPasswordReset)
            lnCancel = findViewById(R.id.lnCancel)
            lnForgotPass = findViewById(R.id.lnForgotPass)
        }

        lnPasswordReset.setOnClickListener {
            if (!utils.hasInternet(this.requireContext())) {
                utils.showToastMessage(forgotPasswordView.context, "Please check your internet connection")
                return@setOnClickListener
            }

//            if (!utils.checkEmail(etEmail.text.toString())) {
//                utils.showToastMessage(forgotPasswordView.context, "Please provide proper email address")
//                return@setOnClickListener
//            }

            utils.showProgress(this.requireContext())

            lnCancel.performClick()

            resetPasswordAPI.resetPassword(etEmail.text.toString()) { it ->
                var successMessage = ""

                if (it!!.success) {
                    successMessage = it?.data?.get(0).toString()

                    resetPasswordAPI.sendEmail(etEmail.text.toString(),"Reset Password",it?.data?.get(1).toString()) { it ->
                        utils.closeProgress()
                        utils.showToastMessage(forgotPasswordView.context, successMessage)
                    }

                } else {
                    utils.showToastMessage(forgotPasswordView.context, it?.messages?.get(0).toString())
                    utils.closeProgress()
                }
            }
        }

        lnCancel.setOnClickListener {
            dialog!!.dismiss()
        }

        return forgotPasswordView
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}