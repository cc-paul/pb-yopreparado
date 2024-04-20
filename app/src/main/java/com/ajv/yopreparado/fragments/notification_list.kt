package com.ajv.yopreparado.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.ProfileActitivity
import com.ajv.yopreparado.R
import com.ajv.yopreparado.recycleview.hotlineAdapter
import com.ajv.yopreparado.recycleview.notificationAdapter
import com.ajv.yopreparado.recycleview.notificationItem
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils
import kotlin.collections.ArrayList

class notification_list(private val profileContext : ProfileActitivity) : Fragment() , ProfileActitivity.IOnBackPressed {
    private lateinit var notificationView: View
    private lateinit var rvNotification: RecyclerView
    private lateinit var imgBack: ImageView

    private var notificationAdapter: notificationAdapter? = null

    private val notificationList  = ArrayList<notificationItem>()
    private val mapAPIService = restApiService()
    private val utils = utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationView = inflater.inflate(R.layout.fragment_notification_list, container, false)
        rvNotification = notificationView.findViewById(R.id.rvNotification)
        imgBack = notificationView.findViewById(R.id.imgBack)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                imgBack.performClick()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        imgBack.setOnClickListener {
            profileContext.goBack()
        }

        loadNotification()

        return notificationView
    }

    fun loadNotification() {
        if (!utils.hasInternet(requireContext())) {
            utils.showToastMessage(requireContext(), "Please check your internet connection")
            return
        }

        mapAPIService.getAllNotification { response ->
            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val notif = data.notification

                    notif.forEach { notif ->
                        notificationList.add(
                            notificationItem(
                                notif.title,
                                notif.body,
                                notif.dateCreated,
                                notif.imageLink
                            )
                        )
                    }

                    notificationAdapter = notificationAdapter(notificationList)
                    rvNotification.layoutManager = LinearLayoutManager(requireContext())
                    rvNotification.adapter = notificationAdapter
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}