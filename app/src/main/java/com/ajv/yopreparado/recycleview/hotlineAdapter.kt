package com.ajv.yopreparado.recycleview

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.R
import com.ajv.yopreparado.fragments.map
import com.bumptech.glide.Glide

class hotlineAdapter(private val hotlineFragment: DialogFragment, var items: ArrayList<hotlinetItem>) : RecyclerView.Adapter<hotlineAdapter.ViewHolder>() {

    private val REQUEST_PHONE_CALL: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hotline, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        val mobileNumber : String = item.mobileNumber.ifEmpty { "Not Available" }
        val telephoneNumber : String = item.telephoneNumber.ifEmpty { "Not Available" }

        holder.tvHotline.text = item.hotline
        holder.tvEmail.text = item.email
        holder.tvMobileNumber.text = mobileNumber
        holder.tvTelephoneNumber.text = telephoneNumber

        if (item.mobileNumber.isNotEmpty()) {
            holder.tvMobileNumber.paintFlags = holder.tvMobileNumber.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }

        if (item.telephoneNumber.isNotEmpty()) {
            holder.tvTelephoneNumber.paintFlags = holder.tvTelephoneNumber.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }

        holder.tvMobileNumber.setOnClickListener {
            if (!holder.tvMobileNumber.text.equals("Not Available")) {
                broadcastCaller(holder.itemView.context,mobileNumber)
            }
        }

        holder.tvTelephoneNumber.setOnClickListener {
            if (!holder.tvTelephoneNumber.text.equals("Not Available")) {
                broadcastCaller(holder.itemView.context,mobileNumber)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHotline : TextView = itemView.findViewById(R.id.tvHotline)
        val tvEmail : TextView = itemView.findViewById(R.id.tvEmail)
        val tvMobileNumber : TextView = itemView.findViewById(R.id.tvMobileNumber)
        val tvTelephoneNumber : TextView = itemView.findViewById(R.id.tvTelephoneNumber)
    }

    fun filterList(filteredList: ArrayList<hotlinetItem>) {
        items = filteredList
        notifyDataSetChanged()
    }

    fun broadcastCaller(context: Context,number: String) {
        val intent = Intent("call")
        intent.apply {
            intent.putExtra("number",number)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}