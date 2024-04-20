package com.ajv.yopreparado.recycleview

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.R
import com.bumptech.glide.Glide

class fileAdapter(var items: ArrayList<fileItem>) : RecyclerView.Adapter<fileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_files,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            tvFileName.text = item.filename
            tvFileType.text = item.type

            crdFile.setOnClickListener {
                openFileInChrome(item.link,holder.itemView.context)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val crdFile : CardView = itemView.findViewById(R.id.crdFile)
        val tvFileName : TextView = itemView.findViewById(R.id.tvFileName)
        val tvFileType : TextView = itemView.findViewById(R.id.tvFileType)
    }

    private fun openFileInChrome(link: String, context: Context) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.`package` = "com.android.chrome" // Specify Chrome package for opening in Chrome

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // If Chrome is not installed, open in default browser or handle accordingly
            intent.`package` = null
            context.startActivity(intent)
        }
    }
}