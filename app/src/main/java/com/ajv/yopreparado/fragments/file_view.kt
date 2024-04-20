package com.ajv.yopreparado.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.ProfileActitivity
import com.ajv.yopreparado.R
import com.ajv.yopreparado.recycleview.faqAdapter
import com.ajv.yopreparado.recycleview.faqItem
import com.ajv.yopreparado.recycleview.fileAdapter
import com.ajv.yopreparado.recycleview.fileItem
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils

class file_view(private val profileContext : ProfileActitivity) : Fragment() , ProfileActitivity.IOnBackPressed {
    private lateinit var fileView:  View
    private lateinit var rvFiles: RecyclerView
    private lateinit var lnBack: LinearLayout

    private val mapAPIService = restApiService()
    private val utils = utils()
    private val fileList  = ArrayList<fileItem>()

    private var fileAdapter: fileAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fileView = inflater.inflate(R.layout.fragment_file_view, container, false)

        fileView.apply {
            rvFiles = findViewById(R.id.rvFiles)
            lnBack = findViewById(R.id.lnBack)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                lnBack.performClick()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        lnBack.setOnClickListener {
            profileContext.goBack()
        }

        LoadFiles()

        return fileView
    }

    private fun LoadFiles() {
        if (!utils.hasInternet(requireContext())) {
            utils.showToastMessage(requireContext(), "Please check your internet connection")
            return
        }

        mapAPIService.getAllFiles { response ->
            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val data = data.files

                    data.forEach { file ->
                        fileList.add(
                            fileItem(
                                file.id,
                                file.filename,
                                file.type,
                                file.link
                            )
                        )
                    }
                }
            }

            fileAdapter = fileAdapter(fileList)
            rvFiles.layoutManager = GridLayoutManager(requireContext(), 2)
            rvFiles.adapter = fileAdapter
        }
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}