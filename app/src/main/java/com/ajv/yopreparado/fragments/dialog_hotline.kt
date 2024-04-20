package com.ajv.yopreparado.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.R
import com.ajv.yopreparado.recycleview.hotlineAdapter
import com.ajv.yopreparado.recycleview.hotlinetItem
import com.ajv.yopreparado.recycleview.municipalityAdapter
import com.ajv.yopreparado.recycleview.municipalityItem
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils
import java.util.Locale

class dialog_hotline : DialogFragment() {

    private lateinit var hotlineView : View
    private lateinit var rvHotline : RecyclerView
    private lateinit var etSearchHotline : EditText

    private var hotlineAdapter: hotlineAdapter? = null

    private val hotlineList = ArrayList<hotlinetItem>()
    private val mapAPIService = restApiService()
    private val utils = utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hotlineView = inflater.inflate(R.layout.fragment_dialog_hotline, container, false)

        rvHotline = hotlineView.findViewById(R.id.rvHotline)
        etSearchHotline = hotlineView.findViewById(R.id.etSearchHotline)

        etSearchHotline.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                filterHotline(s.toString())
            }
        })

        loadHotline()

        return hotlineView
    }

    private fun filterHotline(text: String) {
        val filteredList = ArrayList<hotlinetItem>()
        for (item in hotlineList) {
            if (item.hotline.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }
        hotlineAdapter?.filterList(filteredList)
    }

    fun loadHotline() {
        if (!utils.hasInternet(requireContext())) {
            utils.showToastMessage(requireContext(), "Please check your internet connection")
            return
        }

        mapAPIService.getAllHotline {response ->
            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val hotline = data.hotline

                    hotline.forEach { hotline ->
                        hotlineList.add(
                            hotlinetItem(
                                hotline.hotline,
                                hotline.emailAddress,
                                hotline.mobileNumber,
                                hotline.telephoneNumber
                            )
                        )
                    }

                    hotlineAdapter = hotlineAdapter(this,hotlineList)
                    rvHotline.layoutManager = LinearLayoutManager(requireContext())
                    rvHotline.adapter = hotlineAdapter
                }
            }
        }
    }
}