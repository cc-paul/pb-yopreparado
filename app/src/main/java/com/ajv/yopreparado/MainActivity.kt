package com.ajv.yopreparado

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CALL_PHONE
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.FrameLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.ajv.yopreparado.services.utils
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ajv.yopreparado.fragments.change_password
import com.ajv.yopreparado.fragments.map
import com.ajv.yopreparado.services.locationUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

class MainActivity : AppCompatActivity() {
    lateinit var frMain : FrameLayout
    val utils = utils()

    var hotlineDialogReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val number = intent.getStringExtra("number")
                makeACall(number!!)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frMain = findViewById(R.id.frMain)

        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(hotlineDialogReceiver, IntentFilter("call"))

        val fragMan = supportFragmentManager
        val fragTrans = fragMan.beginTransaction()
        val mapFragment = map()

        fragTrans.add(R.id.frMain,mapFragment)
        fragTrans.commit()
    }

    fun makeACall(mobileNumber : String) {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$mobileNumber")
                startActivity(intent)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                utils.showToastMessage(this@MainActivity,"Some permissions were denied. Unable to use this function")
            }
        }

        TedPermission.with(this@MainActivity)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("GPS is required.\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(CALL_PHONE)
            .check()
    }

    fun enablePermissionAngGetLocation() {
        if (!utils.isGPSEnabled(this)) {
            utils.showToastMessage(this,"Please turn on your GPS")
            return
        }

        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val locationUtils = locationUtils(this@MainActivity)

                locationUtils.getCurrentLocation {
                    val intent = Intent("current_location")
                    intent.apply {
                        intent.putExtra("lat", it?.latitude)
                        intent.putExtra("lng", it?.longitude)
                    }
                    LocalBroadcastManager.getInstance(this@MainActivity).sendBroadcast(intent)
                }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                utils.showToastMessage(this@MainActivity,"Some permissions were denied. Unable to use this function")
            }
        }

        TedPermission.with(this@MainActivity)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("GPS is required.\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(ACCESS_FINE_LOCATION)
            .check()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(hotlineDialogReceiver)
    }
}