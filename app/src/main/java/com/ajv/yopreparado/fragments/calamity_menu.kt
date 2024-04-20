package com.ajv.yopreparado.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.ProfileActitivity
import com.ajv.yopreparado.R
import com.ajv.yopreparado.recycleview.eventMenuAdapter
import com.ajv.yopreparado.recycleview.eventMenuItem
import com.ajv.yopreparado.recycleview.notificationAdapter
import com.ajv.yopreparado.recycleview.notificationItem
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils


class calamity_menu(private val profileContext : ProfileActitivity) : Fragment() , ProfileActitivity.IOnBackPressed {
    private lateinit var eventMenuView : View
    private lateinit var rvCalamityMenu : RecyclerView
    private lateinit var imgBack: ImageView

    private val mapAPIService = restApiService()
    private val utils = utils()
    private val menuList  = ArrayList<eventMenuItem>()

    private var eventMenuAdapter: eventMenuAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventMenuView = inflater.inflate(R.layout.fragment_calamity_menu, container, false)
        rvCalamityMenu = eventMenuView.findViewById(R.id.rvCalamityMenu)
        imgBack = eventMenuView.findViewById(R.id.imgBack)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                imgBack.performClick()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        imgBack.setOnClickListener {
            profileContext.goBack()
        }

        loadMenu()

        return eventMenuView
    }

    fun loadMenu() {
        if (!utils.hasInternet(requireContext())) {
            utils.showToastMessage(requireContext(), "Please check your internet connection")
            return
        }

        mapAPIService.getAllMenu { response ->
            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val data = data.event

                    data.forEach { menu ->
                        menuList.add(
                            eventMenuItem(
                                menu.eventID,
                                menu.event,
                                menu.isActive,
                                menu.dateCreated,
                                menu.description,
                                menu.hasImage,
                                menu.origin.toString(),
                                menu.link
                            )
                        )
                    }
                }
            }

            val layoutManager = GridLayoutManager(requireContext(), 3 )
            rvCalamityMenu.layoutManager = layoutManager
            eventMenuAdapter = eventMenuAdapter(profileContext,menuList)
            rvCalamityMenu.adapter = eventMenuAdapter
        }
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}