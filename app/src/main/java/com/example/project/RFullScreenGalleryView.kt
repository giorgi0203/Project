package com.example.project

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Parcelable
import android.support.v4.util.ArrayMap
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.project.tools.GlideApp
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.gallery_item.view.*

import java.io.File
import java.util.ArrayList
import java.util.Stack

/**
 * Created by realtynaNick on 19.02.18.
 */

class RFullScreenGalleryView(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var files: ArrayList<Parcelable>? = null
    private var touchListener: View.OnTouchListener? = null
    private var adapter: FullScreenGalleryViewAdapter? = null

    val YOUTUBE = "YOUTUBE"
    val IMAGE = "IMAGE"

    fun setFiles(files: ArrayList<Parcelable>) {
        this.files = files
        adapter = FullScreenGalleryViewAdapter()

        setAdapter(adapter)
    }

    override fun getAdapter(): FullScreenGalleryViewAdapter? {
        return adapter
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            return false
        }

    }

    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return (v as? RTouchImageView)?.canScrollHorizontallyFroyo(-dx)
            ?: super.canScroll(v, checkV, dx, x, y)
    }

    fun setOnPhotoTapListener(touchListener: View.OnTouchListener) {
        this.touchListener = touchListener
    }


    inner class FullScreenGalleryViewAdapter : PagerAdapter() {
        internal var recycledViewMap = ArrayMap<String, Stack<View>>()
        internal var recycledImageViews = Stack<View>()
        internal var recycledYoutubeViews = Stack<View>()

        internal var o: Any = FutureCallback<ImageView> { e, result ->
            if (result != null) {
                val imageView = result as RTouchImageView
                imageView.setZoom(1f)
                val progressBar = result.tag as ProgressBar
                progressBar.visibility = View.INVISIBLE
            }
        }

        fun onPause() {
            //            for (View v : recycledYoutubeViews) {
            //                ((CustomYoutubePlayerView) v).onPause();
            //            }
        }

        init {
            recycledViewMap[YOUTUBE] = recycledYoutubeViews
            recycledViewMap[IMAGE] = recycledImageViews
        }

        override fun getCount(): Int {
            return files!!.size
        }

        private fun createOrRecycleView(position: Int): View {
            val recycleView: View


            val viewStack = getCurrentStack(position)
            if (viewStack!!.isEmpty()) {
                val frameLayout = FrameLayout(context)
                frameLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleSmall)
                progressBar.indeterminateDrawable.setColorFilter(Color.parseColor("#01A0E4"), PorterDuff.Mode.SRC_IN)
                val params = FrameLayout.LayoutParams(100, 100)
                val imageView = RTouchImageView(context)

                params.gravity = Gravity.CENTER
                progressBar.layoutParams = params

                frameLayout.addView(imageView)
                frameLayout.tag = imageView
                imageView.tag = progressBar
                frameLayout.addView(progressBar)
                recycleView = frameLayout
                //                }
            } else {
                recycleView = viewStack.pop()
            }


            return recycleView
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val recycledView = createOrRecycleView(position)

            if (isYoutube(position)) {
            } else {
                val imageView = recycledView.tag as RTouchImageView
                imageView.setOnTouchListener(touchListener!!)

                if ((files!![position] as RFile).url != null) {
                    Ion.with(context).load((files!![position] as RFile).url).intoImageView(imageView).setCallback(o as FutureCallback<ImageView>)
                } else {
                    Ion.with(context).load(File((files!![position] as RFile).filePath)).intoImageView(imageView).setCallback(o as FutureCallback<ImageView>)
                }
            }
            container.addView(recycledView)

            return recycledView
        }


        private fun getCurrentStack(position: Int): Stack<View>? {
            return if (isYoutube(position)) recycledViewMap[YOUTUBE] else recycledViewMap[IMAGE]
        }

        fun isYoutube(position: Int): Boolean {
            return (files!![position] as RFile).isYoutube
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val galleryView = container as RFullScreenGalleryView
            val recycleView = `object` as View

            galleryView.removeView(recycleView)
            getCurrentStack(position)!!.push(recycleView)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }



    }


}