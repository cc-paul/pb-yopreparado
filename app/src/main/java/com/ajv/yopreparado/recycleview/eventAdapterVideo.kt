package com.ajv.yopreparado.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.R
import com.ajv.yopreparado.fragments.map
import com.ajv.yopreparado.fragments.video_list
import com.bumptech.glide.Glide

class eventAdapterVideo(private val videoFragment: video_list, var items: ArrayList<eventItem>) : RecyclerView.Adapter<eventAdapterVideo.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_events_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.itemView.context)
            .load(item.link)
            .into(holder.imgEventLogo)

        holder.tvEventName.text = item.eventName
        holder.crdEvent.tag = item.eventId

        holder.crdEvent.setOnClickListener {
            videoFragment.apply {
                etSearchVideo.apply {
                    setText("")
                    clearFocus()
                }

                isTyping = false
                eventID = item.eventId
                eventName = item.eventName
                loadVideos()
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgEventLogo : ImageView = itemView.findViewById(R.id.imgEventLogo)
        val tvEventName : TextView = itemView.findViewById(R.id.tvEventName)
        val crdEvent : CardView = itemView.findViewById(R.id.crdEvent)
    }
}