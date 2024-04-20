package com.ajv.yopreparado.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.ajv.yopreparado.R
import com.ajv.yopreparado.data.registrationData
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils
import com.google.firebase.messaging.FirebaseMessaging

class registration : Fragment() {

    lateinit var lnBack : LinearLayout
    lateinit var lnLoginLayout : LinearLayout
    lateinit var lnRegister : LinearLayout
    lateinit var etFirstName : EditText
    lateinit var etMiddleName : EditText
    lateinit var etLastName : EditText
    lateinit var etUsername : EditText
    lateinit var etEmailAddress : EditText
    lateinit var etPassword : EditText
    lateinit var etConfirmPassword : EditText

    var fcmToken : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val registrationView : View = inflater.inflate(R.layout.fragment_registration, container, false)
        val regAPIService = restApiService()
        val utils = utils()
        val firebaseMessaging = FirebaseMessaging.getInstance()

        lnBack = registrationView.findViewById(R.id.lnBack)
        lnLoginLayout = registrationView.findViewById(R.id.lnLoginLayout)
        lnRegister = registrationView.findViewById(R.id.lnRegister)
        etFirstName = registrationView.findViewById(R.id.etFirstName)
        etMiddleName = registrationView.findViewById(R.id.etMiddleName)
        etLastName = registrationView.findViewById(R.id.etLastName)
        etUsername = registrationView.findViewById(R.id.etUsername)
        etEmailAddress = registrationView.findViewById(R.id.etEmailAddress)
        etPassword = registrationView.findViewById(R.id.etPassword)
        etConfirmPassword = registrationView.findViewById(R.id.etConfirmPassword)

        lnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        lnRegister.setOnClickListener {
            if (!utils.hasInternet(this.requireContext())) {
                utils.showSnackMessage(lnLoginLayout,"Please check your internet connection")
                return@setOnClickListener
            }

            if (fcmToken == "") {
                utils.showSnackMessage(lnLoginLayout,"No FCM created. Please contact support")
                return@setOnClickListener
            }

            utils.showProgress(this.requireContext())

            val userInfo = registrationData(
                firstName = etFirstName.text.toString(),
                middlename = etMiddleName.text.toString(),
                lastname = etLastName.text.toString(),
                username = etUsername.text.toString(),
                password = etPassword.text.toString(),
                rpassword = etConfirmPassword.text.toString(),
                emailaddress = etEmailAddress.text.toString(),
                fcm = fcmToken
            )

            regAPIService.addUser(userInfo) {
                utils.closeProgress()

                if (!it!!.success) {

                    var errorMessage = if (it.data != null) {
                        it.data.joinToString("\n")
                    } else {
                        it?.messages?.get(0).toString()
                    }

                    utils.showSnackMessage(lnLoginLayout,errorMessage)
                } else {
                    utils.showToastMessage(requireContext().applicationContext, it?.messages?.get(0).toString())
                    lnBack.performClick()
                }
            }
        }

        firebaseMessaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fcmToken = task.result
            }
        }

        return registrationView
    }
}