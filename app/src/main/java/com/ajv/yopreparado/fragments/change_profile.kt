package com.ajv.yopreparado.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ajv.yopreparado.ProfileActitivity
import com.ajv.yopreparado.R
import com.ajv.yopreparado.data.passwordData
import com.ajv.yopreparado.data.profileData
import com.ajv.yopreparado.helper.sharedHelper
import com.ajv.yopreparado.services.locationUtils
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class change_profile(private val profileContext : ProfileActitivity) : Fragment(),ProfileActitivity.IOnBackPressed {
    private lateinit var profileView : View
    private lateinit var imgBack : ImageView
    private lateinit var imgProfile : ImageView
    private lateinit var lnSelectImage : LinearLayout
    private lateinit var lnSaveProfile : LinearLayout
    private lateinit var lnProfile : LinearLayout

    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    var imageUri : Uri? = null
    var isImageSelected : Boolean = false

    val firebaseStorage : FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference : StorageReference = firebaseStorage.reference

    private val changeProfileService = restApiService()
    private val utils = utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileView = inflater.inflate(R.layout.fragment_change_profile, container, false)

        imgBack = profileView.findViewById(R.id.imgBack)
        imgProfile = profileView.findViewById(R.id.imgProfile)
        lnSelectImage = profileView.findViewById(R.id.lnSelectImage)
        lnSaveProfile = profileView.findViewById(R.id.lnSaveProfile)
        lnProfile = profileView.findViewById(R.id.lnProfile)

        val currentImageLink : String = sharedHelper.getString("image_link")

        regActivityForResult()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                imgBack.performClick()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        imgBack.setOnClickListener {
            profileContext.goBack()
        }

        lnSelectImage.setOnClickListener {
            val permissionlistener: PermissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                    val imageSelectIntent = Intent()

                    imageSelectIntent.apply {
                        type = "image/*"
                        action = Intent.ACTION_GET_CONTENT
                    }
                    activityResultLauncher.launch(imageSelectIntent)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    utils.showSnackMessage(lnProfile,"Some permissions were denied. Unable to use this function")
                }
            }

            TedPermission.with(requireContext())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Storage is required.\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()
        }

        lnSaveProfile.setOnClickListener {
            if (!isImageSelected) {
                utils.showSnackMessage(lnProfile,"Please select an image first")
            } else {
                val imageReference = storageReference.child("profiles").child(sharedHelper.getInt("user_id").toString())

                utils.showProgress(requireContext())

                imageReference.putFile(imageUri!!).addOnSuccessListener {
                    val linkRef = storageReference.child("profiles").child(sharedHelper.getInt("user_id").toString())

                    linkRef.downloadUrl.addOnSuccessListener { url ->
                        isImageSelected = false
                        utils.closeProgress()

                        val profileInfo = profileData(
                            mode = "change_profile",
                            image_link = url.toString()
                        )
                        changeProfileService.changeProfile(profileInfo) { it ->
                            utils.showSnackMessage(lnProfile, it!!.messages[0])

                            if (it!!.success) {
                                var imageLink : String = url.toString()

                                sharedHelper.putString("image_link",imageLink).toString()

                                Glide.with(this)
                                    .load(Uri.parse(imageLink))
                                    .into(imgProfile)

                            } else {
                                Glide.with(this)
                                    .load(R.drawable.user)
                                    .into(imgProfile)
                            }
                        }
                    }.addOnFailureListener {
                        utils.showSnackMessage(lnProfile,"There was an error changing profile. Please try again later 1")
                    }

                }.addOnFailureListener {
                    utils.closeProgress()
                    utils.showSnackMessage(lnProfile,"Error updating profile. Please try again later")
                }
            }
        }

        if (currentImageLink != "-") {
            Glide.with(this)
                .load(Uri.parse(currentImageLink))
                .into(imgProfile)
        }

        return profileView
    }

    fun regActivityForResult() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                val resultCode = result.resultCode
                val imageData = result.data

                if (resultCode == RESULT_OK && imageData != null) {
                    imageUri = imageData.data

                    Glide.with(this)
                        .load(imageUri)
                        .into(imgProfile)

                    isImageSelected = true
                }
            })
    }

    override fun onBackPressed(): Boolean {
        return true
    }


}