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

class videoListAdapter(var videoListFragment: video_list,var items: ArrayList<videoListItem>) : RecyclerView.Adapter<videoListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_videos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.itemView.context)
            .load(item.imageLink)
            .into(holder.imgVideo)

        holder.tvVideoTitle.text = item.videoTitle
        holder.tvVideoView.text = item.views + " Views"

        holder.imgPlay.setOnClickListener {
            videoListFragment.loadVideo(item.videoLink,item.videoTitle,item.id)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgVideo : ImageView = itemView.findViewById(R.id.imgVideo)
        val imgPlay : ImageView = itemView.findViewById(R.id.imgPlay)
        val tvVideoTitle : TextView = itemView.findViewById(R.id.tvVideoTitle)
        val tvVideoView : TextView = itemView.findViewById(R.id.tvVideoView)
    }
}