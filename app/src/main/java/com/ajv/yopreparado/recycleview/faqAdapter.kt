package com.ajv.yopreparado.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajv.yopreparado.R
import com.bumptech.glide.Glide

class faqAdapter(var items: ArrayList<faqItem>) : RecyclerView.Adapter<faqAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvQuestion.text = item.question
        holder.tvAnswer.text = item.answer
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion : TextView = itemView.findViewById(R.id.tvQuestion)
        val tvAnswer : TextView = itemView.findViewById(R.id.tvAnswer)
    }
}