package com.example.project.module.user.dashboard.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.project.R
import com.example.project.module.user.Image
import com.example.project.module.user.dashboard.adapters.GalleryRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_gallery.*


/**
 * A simple [Fragment] subclass.
 *
 */
class GalleryFragment : Fragment() {

    val images: ArrayList<Image> = ArrayList()
    var adapter: GalleryRecyclerViewAdapter? = null

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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryView.layoutManager = LinearLayoutManager(context)
        adapter = GalleryRecyclerViewAdapter(images)
        galleryView.adapter = adapter
        adapter!!.notifyDataSetChanged()

    }

    private fun addData() {
        images.add(Image("https://picsum.photos/200/300","a","a","a"))
        images.add(Image("https://picsum.photos/200/300","a","a","a"))
    }


}
