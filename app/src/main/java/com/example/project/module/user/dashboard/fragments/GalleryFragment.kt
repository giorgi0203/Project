package com.example.project.module.user.dashboard.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.project.R
import com.example.project.module.user.Image
import com.example.project.module.user.dashboard.adapters.GalleryRecyclerViewAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_gallery.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.PopupWindow
//import android.R
import android.widget.Button


/**
 * A simple [Fragment] subclass.
 *
 */
class GalleryFragment : Fragment() {

    val images: ArrayList<Image> = ArrayList()
    var adapter: GalleryRecyclerViewAdapter? = null

    var popUp: PopupWindow? = null
    var layout: LinearLayout? = null
    var tv: TextView? = null
    var params: ViewGroup.LayoutParams? = null
    var mainLayout: LinearLayout? = null
    var but: Button? = null
    var click = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        init()
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    private fun init() {
        addData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("adapter", "Error getting documents: ")

        galleryView.layoutManager = GridLayoutManager(context,3)
//        if (activity != null){
            adapter = GalleryRecyclerViewAdapter(activity!!,images)
//        }

        galleryView.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }

    private fun addData() {
        val db = FirebaseFirestore.getInstance()

        db.collection("gallery")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    images.add(Image(document.get("url").toString(),document.get("title").toString(),document.get("description").toString(),document.get("userId").toString()))
                }
                adapter!!.notifyDataSetChanged()
            }
            .addOnFailureListener {
                //                Log.d(TAG, "Error getting documents: ", exception)
            }
    }


}
