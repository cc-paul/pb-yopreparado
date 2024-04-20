package com.ajv.yopreparado.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.R
import com.bumptech.glide.Glide

class doAdapter(var items: ArrayList<doItem>) : RecyclerView.Adapter<doAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dos,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvDos.text = item.`do`

        Glide.with(holder.itemView.context)
            .load(if (item.isDo == 1) R.drawable.icn_circle else R.drawable.icn_circle_red)
            .into(holder.imgIsDo)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvDos : TextView = itemView.findViewById(R.id.tvDos)
        val imgIsDo : ImageView = itemView.findViewById(R.id.imgIsDo)
    }
}