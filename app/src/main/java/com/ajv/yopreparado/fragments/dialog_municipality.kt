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
import com.ajv.yopreparado.recycleview.eventAdapter
import com.ajv.yopreparado.recycleview.eventItem
import com.ajv.yopreparado.recycleview.municipalityAdapter
import com.ajv.yopreparado.recycleview.municipalityItem
import com.ajv.yopreparado.services.restApiService
import com.ajv.yopreparado.services.utils
import java.util.Locale


class dialog_municipality : DialogFragment() {
    private lateinit var rvMunicipalities: RecyclerView
    private lateinit var etSearchMunicipality : EditText

    private val municipalityList = ArrayList<municipalityItem>()
    private val filteredMunicipalityList = ArrayList<municipalityItem>()
    private var municipalityAdapter: municipalityAdapter? = null
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
        val municipalityView : View = inflater.inflate(R.layout.fragment_dialog_municipality, container, false)

        rvMunicipalities = municipalityView.findViewById(R.id.rvMunicipalities)
        etSearchMunicipality = municipalityView.findViewById(R.id.etSearchMunicipality)
        loadMunicipalities()

        etSearchMunicipality.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                filterMunicipalities(s.toString())
            }
        })

        return municipalityView
    }

    private fun filterMunicipalities(text: String) {
        val filteredList = ArrayList<municipalityItem>()
        for (item in municipalityList) {
            if (item.barangayName.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }
        municipalityAdapter?.filterList(filteredList)
    }

    fun loadMunicipalities() {
        if (!utils.hasInternet(requireContext())) {
            utils.showToastMessage(requireContext(), "Please check your internet connection")
            return
        }

        mapAPIService.getAllMunicipality { response ->
            if (response!!.success) {
                val data = response.data

                if (data.rows_returned != 0) {
                    val municipality = data.municipality

                    municipality.forEach {municipality ->
                        municipalityList.add(
                            municipalityItem(
                                municipality.id,
                                municipality.barangayName,
                                municipality.totalPopulation,
                                municipality.isActive,
                                municipality.lat,
                                municipality.lng
                            )
                        )
                    }
                    filteredMunicipalityList.addAll(municipalityList)

                    municipalityAdapter = municipalityAdapter(this,municipalityList)
                    rvMunicipalities.layoutManager = LinearLayoutManager(requireContext())
                    rvMunicipalities.adapter = municipalityAdapter
                }
            }
        }
    }
}