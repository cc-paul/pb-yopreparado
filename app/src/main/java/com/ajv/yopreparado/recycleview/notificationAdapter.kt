package com.ajv.yopreparado.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.R
import com.bumptech.glide.Glide

class notificationAdapter(var items: ArrayList<notificationItem>) : RecyclerView.Adapter<notificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notifications,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.itemView.context)
            .load(item.image_link)
            .into(holder.imgEventIcon)

        holder.tvTitle.text = item.title
        holder.tvBody.text = item.body
        holder.tvAgo.text = item.ago
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imgEventIcon : ImageView = itemView.findViewById(R.id.imgEventIcon)
        val tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        val tvBody : TextView = itemView.findViewById(R.id.tvBody)
        val tvAgo : TextView = itemView.findViewById(R.id.tvAgo)
    }
}