package com.example.project.tools

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.project.R

//import cn.jzvd.VideoPlayer
//import com.koushikdutta.ion.Ion
//import com.realtyfeed.android.R
//import com.realtyfeed.android.models.RPostPhotosModel
//import com.realtyfeed.android.ui.activities.FullScreenPhotoActivity
//import com.squareup.picasso.Picasso
//import kotlinx.android.synthetic.main.post_photos_gallery_view_layout.view.*

class RPostPhotosGalleryView : FrameLayout {

    constructor(context: Context) : super(context, null) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        View.inflate(context, R.layout.post_photos_gallery_view_layout, this)
    }


    private var postPhotosArrayList: ArrayList<RPostPhotosModel>? = null

    fun setData(postPhotosArrayList: ArrayList<RPostPhotosModel>) {
        this.postPhotosArrayList = postPhotosArrayList
        val size = postPhotosArrayList.size
//        if (size > 4) {
//            addPostItem(firstContainer, postPhotosArrayList[0], false)
//            addPostItem(secondContainer, postPhotosArrayList[1], false)
//            addPostItem(thirdContainer, postPhotosArrayList[2], false)
//            addPostItem(forthContainer, postPhotosArrayList[3], false)
//            viewStatus(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE)
//        } else
//            if (size == 4) {
//            addPostItem(firstContainer, postPhotosArrayList[0], false)
//            addPostItem(secondContainer, postPhotosArrayList[1], false)
//            addPostItem(thirdContainer, postPhotosArrayList[2], false)
//            addPostItem(forthContainer, postPhotosArrayList[3], false)
//            viewStatus(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE)
//        }
        if (size > 3) {
            addPostItem(firstContainer, postPhotosArrayList[0], false)
            addPostItem(secondContainer, postPhotosArrayList[1], false)
            addPostItem(thirdContainer, postPhotosArrayList[2], false)
            viewStatus(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE)
            morePhotosTextView.text = postPhotosArrayList.size.toString()
        } else if (size == 3) {
            addPostItem(firstContainer, postPhotosArrayList[0], false)
            addPostItem(secondContainer, postPhotosArrayList[1], false)
            addPostItem(thirdContainer, postPhotosArrayList[2], false)
            viewStatus(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE)
        } else if (size == 2) {
            addPostItem(firstContainer, postPhotosArrayList[0], false)
            addPostItem(secondContainer, postPhotosArrayList[1], false)
            viewStatus(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.VISIBLE, View.GONE)
        } else if (size == 1) {
            addPostItem(firstContainer, postPhotosArrayList[0], true)
            viewStatus(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE)
        }

        firstContainer.setOnClickListener {
            setItemsListeneter(postPhotosArrayList, 0)
        }
        secondContainer.setOnClickListener {
            setItemsListeneter(postPhotosArrayList, 1)
        }
        thirdContainer.setOnClickListener {
            setItemsListeneter(postPhotosArrayList, 2)
        }
//        forthContainer.setOnClickListener {
//            setItemsListeneter(postPhotosArrayList, 3)
//        }
    }

    private fun setItemsListeneter(postPhotosArrayList: ArrayList<RPostPhotosModel>, position: Int) {
        val intent = Intent(context, FullScreenPhotoActivity::class.java)
        intent.putParcelableArrayListExtra("files", postPhotosArrayList)
        intent.putExtra("position", position)
        context.startActivity(intent, ActivityOptions.makeCustomAnimation(context, R.anim.abc_fade_in, R.anim.abc_fade_out).toBundle())
    }

    private fun viewStatus(first: Int, second: Int, third: Int, third_: Int, right: Int, morePhotos: Int) {
        firstContainer.visibility = first
        secondContainer.visibility = second
        thirdContainer.visibility = third
//        forthContainer.visibility = forth
        rightContainer.visibility = right
        thirdFrameLayoutContainer.visibility = third_
        morePhotosTextView.visibility = morePhotos
    }

    private fun addPostItem(container: FrameLayout, postPhotosModel: RPostPhotosModel, isCountOne: Boolean) {
        container.removeAllViews()
        if (isCountOne && postPhotosModel.isVideo) {
            val videoPlayer = VideoPlayer(context)
            videoPlayer.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            videoPlayer.setUp(postPhotosModel.videoUrl, postPhotosModel.videoTitle, VideoPlayer.SCREEN_WINDOW_NORMAL)
            Ion.with(videoPlayer.thumbImageView).placeholder(R.mipmap.empty_photo).load(postPhotosModel.coverUrl)
            container.addView(videoPlayer)
        } else {
            val playVideoView = RVideoPlayerPlayView(context)
            playVideoView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            Ion.with(playVideoView.coverImageView).placeholder(R.mipmap.empty_photo).load(postPhotosModel.coverUrl)
            Picasso.get().load(postPhotosModel.coverUrl).placeholder(R.mipmap.empty_photo).error(R.mipmap.empty_photo).into(playVideoView.coverImageView)

            if (postPhotosModel.isVideo)
                playVideoView.playImageView!!.visibility = View.VISIBLE
            else
                playVideoView.playImageView!!.visibility = View.GONE
            container.addView(playVideoView)
        }
    }
}
