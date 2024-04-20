package com.ajv.yopreparado.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ajv.yopreparado.MainActivity
import com.ajv.yopreparado.R
import com.ajv.yopreparado.data.loginData
import com.ajv.yopreparado.helper.sharedHelper
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils
import com.google.firebase.messaging.FirebaseMessaging


class login : Fragment() {

    lateinit var tvSignUp : TextView
    lateinit var etEmailUser : EditText
    lateinit var etPassword : EditText
    lateinit var lnLogin : LinearLayout
    lateinit var layoutLogin : ConstraintLayout
    lateinit var imgLogo : ImageView
    lateinit var tvForgotPassword : TextView

    var fcmToken : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loginView : View = inflater.inflate(R.layout.fragment_login, container, false)
        val loginAPIService = restApiService()
        val utils = utils()
        val firebaseMessaging = FirebaseMessaging.getInstance()

        tvSignUp = loginView.findViewById(R.id.tvSignUp)
        etEmailUser = loginView.findViewById(R.id.etEmailUser)
        etPassword = loginView.findViewById(R.id.etPassword)
        lnLogin = loginView.findViewById(R.id.lnLogin)
        layoutLogin = loginView.findViewById(R.id.layoutLogin)
        imgLogo = loginView.findViewById(R.id.imgLogo)
        tvForgotPassword = loginView.findViewById(R.id.tvForgotPassword)

        imgLogo.setOnLongClickListener {
            etEmailUser.setText("janicedoe")
            etPassword.setText("YL{WDAg8")
            lnLogin.performClick()
            true
        }

        lnLogin.setOnClickListener {
            Log.e("FCM",fcmToken)

            if (fcmToken == "") {
                utils.showSnackMessage(layoutLogin,"Something went wrong. Please contact support")
                return@setOnClickListener
            }

            if (!utils.hasInternet(this.requireContext())) {
                utils.showSnackMessage(layoutLogin,"Please check your internet connection")
                return@setOnClickListener
            }

            utils.showProgress(this.requireContext())

            val loginInfo = loginData(
                username = etEmailUser.text.toString(),
                password = etPassword.text.toString(),
                fcmtoken = fcmToken
            )

            loginAPIService.loginUser(loginInfo) { it ->
                utils.closeProgress()
                utils.showSnackMessage(layoutLogin,it?.messages?.get(0).toString())

                if (it!!.success) {
                    sharedHelper.putInt("session_id",it?.data!!.session_id)
                    sharedHelper.putString("access_token",it?.data!!.access_token)
                    sharedHelper.putInt("access_token_expires_in",it?.data!!.access_token_expires_in)
                    sharedHelper.putString("refresh_token",it?.data!!.refresh_token)
                    sharedHelper.putInt("refresh_token_expires_in",it?.data!!.refresh_token_expires_in)
                    sharedHelper.putString("first_name",it?.data!!.first_name)
                    sharedHelper.putString("middle_name",it?.data!!.middle_name)
                    sharedHelper.putString("last_name",it?.data!!.last_name)
                    sharedHelper.putString("email_address",it?.data!!.email_address)
                    sharedHelper.putInt("user_id",it?.data!!.user_id.toInt())
                    sharedHelper.putString("image_link",it?.data!!.image_link.toString())

                    activity?.let{
                        val intent = Intent (it, MainActivity::class.java)
                        it.startActivity(intent)
                    }
                    activity?.finish()
                }
            }
        }

        tvForgotPassword.setOnClickListener {
            val fragMan : FragmentManager = requireActivity().supportFragmentManager
            val forgotPasswordDialog = dialog_forgotpass()
            forgotPasswordDialog.show(fragMan,"ForgotPassFragment")
        }

        tvSignUp.setOnClickListener {
            val fragMan : FragmentManager = requireActivity().supportFragmentManager
            val fragTrans : FragmentTransaction = fragMan.beginTransaction()
            val registrationFrag = registration()

            fragTrans.add(R.id.frLogin,registrationFrag)
            fragTrans.addToBackStack(null)
            fragTrans.commit()
        }


        firebaseMessaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fcmToken = task.result
            }
        }

        return loginView
    }
}