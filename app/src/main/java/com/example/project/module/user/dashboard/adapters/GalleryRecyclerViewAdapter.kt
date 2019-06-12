package com.example.project.module.user.dashboard.adapters

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.project.FullScreenPhotoActivity
//import com.bumptech.glide.Glide
import com.example.project.R
import com.example.project.RFile
import com.example.project.module.user.Image
import com.example.project.tools.GlideApp
import com.google.firebase.storage.FirebaseStorage
//import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.gallery_item.view.*

class GalleryRecyclerViewAdapter(private val activity: FragmentActivity, private val data: ArrayList<Image>) :
    RecyclerView.Adapter<GalleryRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.titleTextView.text = data[position].title

        val storageRef = FirebaseStorage.getInstance().reference

        GlideApp.with(activity)
            .load(storageRef.child(data[position].url))
            .into(holder.itemView.imageHolder)

        holder.itemView.setOnClickListener {
            val intent = Intent(activity,FullScreenPhotoActivity::class.java)
            val files = ArrayList<RFile>()
            val file = RFile()
            file.url = "http://139.162.207.17/uploads/apartments/8c3c0ada-0875-45bf-b44e-4c96821df103.jpg"
            file.id = 0
            files.add(file)
            intent.putParcelableArrayListExtra("files",files)
            intent.putExtra("position",0)
            activity.startActivity(intent)

        }
    }

    inner class Holder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView)
}