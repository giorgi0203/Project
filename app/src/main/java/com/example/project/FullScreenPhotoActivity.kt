package com.example.project

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.os.Handler
import android.os.Parcelable
import android.support.v4.view.ViewPager
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView



class FullScreenPhotoActivity : AppCompatActivity(), View.OnTouchListener, ViewPager.OnPageChangeListener {

    private var fullScreenImageDescriptionLayout: FrameLayout? = null
    private var fullScreenBlackBackgroundView: View? = null
    private var galleryView: RFullScreenGalleryView? = null
    private var upperLabel: TextView? = null
    private var shareImg: ImageView? = null
    private var files: List<RFile>? = null
    private var shareUrl: String? = null

    internal var downTime: Long = 0
    internal var tapCount = 0
    internal var lastY = -1f
    internal var lastX = -1f

    internal var handler = Handler()

    internal var singTapRunnable: Runnable = object : Runnable {
        override fun run() {
            if (tapCount % 2 == 1) {
                toggleHUDVisibility()
            }
            tapCount = 0
            handler.removeCallbacks(this)
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_photo)

        galleryView = findViewById<View>(R.id.full_screen_gallery_view) as RFullScreenGalleryView
        files = intent.getParcelableArrayListExtra("files")
        galleryView!!.setFiles(intent.getParcelableArrayListExtra<Parcelable>("files"))
        val pos = intent.getIntExtra("position", 0)


        if (files!![pos].isYoutube) {
            shareUrl = "www.youtube.com/watch?v=" + files!![pos].url
        } else {
            shareUrl = files!![pos].url
        }
        galleryView!!.setCurrentItem(pos, false)
        galleryView!!.setOnPhotoTapListener(this)
        galleryView!!.addOnPageChangeListener(this)

        fullScreenBlackBackgroundView = findViewById(R.id.full_screen_black_background)
        fullScreenImageDescriptionLayout = findViewById<View>(R.id.full_screen_image_description) as FrameLayout
        upperLabel = findViewById<View>(R.id.upper_tv) as TextView
        shareImg = findViewById<View>(R.id.share_img) as ImageView
        shareImg!!.setOnClickListener {

        }
    }

    private fun toggleHUDVisibility(forceShow: Boolean = false) {
        if (fullScreenImageDescriptionLayout!!.tag == null && !forceShow) {
            //GO BLACK
            fullScreenBlackBackgroundView!!.animate().alpha(1f)
            fullScreenImageDescriptionLayout!!.animate().alpha(0f)
            fullScreenImageDescriptionLayout!!.tag = true
            if (Build.VERSION.SDK_INT >= 16) {
                val decorView = window.decorView
                val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
                decorView.systemUiVisibility = uiOptions
            }
        } else {
            //GO WHITE
            fullScreenBlackBackgroundView!!.animate().alpha(0f)
            fullScreenImageDescriptionLayout!!.animate().alpha(1f)
            fullScreenImageDescriptionLayout!!.tag = null
            if (Build.VERSION.SDK_INT >= 16) {
                val decorView = window.decorView
                val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
                decorView.systemUiVisibility = uiOptions
            }
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            lastY = event.y
            lastX = event.x
            downTime = System.currentTimeMillis()
            tapCount++
        } else if (event.action == MotionEvent.ACTION_UP) {
            if (System.currentTimeMillis() - downTime < 150) {
                handler.removeCallbacks(singTapRunnable)
                handler.postDelayed(singTapRunnable, 150)
                return true
            }
        } else if (event.action == MotionEvent.ACTION_MOVE) {
            if (lastY >= 0 && lastX >= 0) {
//                val dimen = resources.getDimensionPixelSize(R.dimen.gallery_view_tap_distance)
                val dimen = 100
                if (Math.abs(lastY - event.y) >= dimen || Math.abs(lastX - event.x) >= dimen) {
                    tapCount = 0
                }
            }

        }
        return false
    }

    override fun onPause() {
        super.onPause()
        try {
            galleryView!!.adapter!!.onPause()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        tapCount = 0
        if (fullScreenImageDescriptionLayout!!.tag != null && galleryView!!.adapter!!.isYoutube(position)) {
            toggleHUDVisibility(true)
        }
        if (galleryView!!.adapter!!.isYoutube(position)) {
            shareUrl = "www.youtube.com/watch?v=" + files!![position].url
        } else {
            shareUrl = files!![position].url
        }
        upperLabel!!.text = files!![position].caption


    }

    override fun onPageScrollStateChanged(state: Int) {

    }


}