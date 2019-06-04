package com.example.project.module.user.dashboard.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.project.R
import com.example.project.module.user.Image
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.gallery_item.view.*

class GalleryRecyclerViewAdapter(private val data: ArrayList<Image>) : RecyclerView.Adapter<GalleryRecyclerViewAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.titleTextView.text = data[position].title

//        Ion.with(imageView)
//            .load("http://example.com/image.png");

    }

    inner class Holder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView)
}