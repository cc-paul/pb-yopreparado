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
import com.ajv.yopreparado.recycleview.faqAdapter
import com.ajv.yopreparado.recycleview.faqItem
import com.ajv.yopreparado.recycleview.notificationAdapter
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils

class faq(private val profileContext : ProfileActitivity) : Fragment() , ProfileActitivity.IOnBackPressed {
    private lateinit var faqView:  View
    private lateinit var rvFAQ: RecyclerView
    private lateinit var imgBack: ImageView

    private val mapAPIService = restApiService()
    private val utils = utils()
    private val faqList  = ArrayList<faqItem>()

    private var faqAdapter: faqAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        faqView = inflater.inflate(R.layout.fragment_faq, container, false)
        rvFAQ = faqView.findViewById(R.id.rvFAQ)
        imgBack = faqView.findViewById(R.id.imgBack)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                imgBack.performClick()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        imgBack.setOnClickListener {
            profileContext.goBack()
        }

        loadFAQ()
        return faqView
    }

    fun loadFAQ() {
        if (!utils.hasInternet(requireContext())) {
            utils.showToastMessage(requireContext(), "Please check your internet connection")
            return
        }

        mapAPIService.getFAQList { response ->
            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val data = data.faq

                    data.forEach { faq ->
                        faqList.add(
                            faqItem(
                                faq.question,
                                faq.answer
                            )
                        )
                    }
                }
            }

            faqAdapter = faqAdapter(faqList)
            rvFAQ.layoutManager = LinearLayoutManager(requireContext())
            rvFAQ.adapter = faqAdapter
        }
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}