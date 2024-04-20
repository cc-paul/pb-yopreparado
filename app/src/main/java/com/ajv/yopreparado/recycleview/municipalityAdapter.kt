package com.ajv.yopreparado.recycleview

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.R
import com.ajv.yopreparado.fragments.map
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class municipalityAdapter( private val muniDialogFragment: DialogFragment ,var items: ArrayList<municipalityItem>) : RecyclerView.Adapter<municipalityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_municipality, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvMunicipalityName.text = "Brgy. ${item.barangayName}"
        holder.tvPopulation.text = "Population : ${item.totalPopulation}"

        holder.crdMunicipality.setOnClickListener {
            val intent = Intent("municipalities")
            intent.apply {
                intent.putExtra("municipality",holder.tvMunicipalityName.text)
                intent.putExtra("lat", item.lat)
                intent.putExtra("lng", item.lng)
            }
            LocalBroadcastManager.getInstance(holder.itemView.context).sendBroadcast(intent)

            muniDialogFragment.dismiss()
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val crdMunicipality : CardView = itemView.findViewById(R.id.crdMunicipality)
        val tvMunicipalityName : TextView = itemView.findViewById(R.id.tvMunicipalityName)
        val tvPopulation : TextView = itemView.findViewById(R.id.tvPopulation)
    }

    fun filterList(filteredList: ArrayList<municipalityItem>) {
        items = filteredList
        notifyDataSetChanged()
    }
}