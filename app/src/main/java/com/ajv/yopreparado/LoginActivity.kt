package com.ajv.yopreparado

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ajv.yopreparado.fragments.login
import com.ajv.yopreparado.fragments.splash
import java.util.Timer
import kotlin.concurrent.schedule

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val splashFragMan : FragmentManager = supportFragmentManager
        val splashFragTrans : FragmentTransaction = splashFragMan.beginTransaction()
        val splashFrag = splash()

        splashFragTrans.add(R.id.frLogin,splashFrag)
        splashFragTrans.commit()

        Timer().schedule(3000) {
            val loginFragMan : FragmentManager = supportFragmentManager
            val loginFragTrans : FragmentTransaction = loginFragMan.beginTransaction()
            val loginFrag = login()

            loginFragTrans.replace(R.id.frLogin,loginFrag)
            loginFragTrans.commit()
        }
    }
}