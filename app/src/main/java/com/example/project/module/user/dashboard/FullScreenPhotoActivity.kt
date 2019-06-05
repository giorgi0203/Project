package com.example.project.module.user.dashboard
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.support.v4.view.ViewPager
import android.util.Log.d
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.project.R
import com.example.project.tools.RPostPhotosGalleryView

//import cn.jzvd.BaseVideoPlayer
//import com.realtyfeed.android.R
//import com.realtyfeed.android.models.RPostPhotosModel
//import com.realtyfeed.android.ui.customviews.RFullScreenGalleryView
//import com.realtyfeed.android.ui.tools.Tools

class FullScreenPhotoActivity : AppCompatActivity(), View.OnTouchListener, ViewPager.OnPageChangeListener {

    private var fullScreenImageDescriptionLayout: FrameLayout? = null
    private var fullScreenBlackBackgroundView: View? = null
    private var galleryView: RPostPhotosGalleryView? = null
    private var upperLabel: TextView? = null
    private var shareImg: ImageView? = null
    private var files: List<ImageView>? = null
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

        galleryView = findViewById<View>(R.id.full_screen_gallery_view) as RPostPhotosGalleryView
        files = intent.getParcelableArrayListExtra("files")
        val pos = intent.getIntExtra("position", 0)
        galleryView!!.setFiles(intent.getParcelableArrayListExtra<Parcelable>("files"), pos)


        if (files!![pos].isVideo) {
            shareUrl = files!![pos].videoUrl
        } else {
            shareUrl = files!![pos].coverUrl
        }

        galleryView!!.setCurrentItem(pos, false)
        galleryView!!.setOnPhotoTapListener(this)
        galleryView!!.addOnPageChangeListener(this)


        fullScreenBlackBackgroundView = findViewById(R.id.full_screen_black_background)
        fullScreenImageDescriptionLayout = findViewById<View>(R.id.full_screen_image_description) as FrameLayout
        upperLabel = findViewById<View>(R.id.upper_tv) as TextView
        shareImg = findViewById<View>(R.id.share_img) as ImageView
        shareImg!!.setOnClickListener {
            Tools.shareInstance(shareUrl.toString(), resources.getString(R.string.share_via))
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
                val dimen = resources.getDimensionPixelSize(R.dimen.gallery_view_tap_distance)
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
        d("onPageSelected", " " + position)
//        if (files!![position].isVideo) {
//            BaseVideoPlayer.releaseAllVideos()
////            val videoPlayer = galleryView!!.getChildAt(position) as VideoPlayer
////            videoPlayer.startVideo()
//        }

        tapCount = 0
        if (fullScreenImageDescriptionLayout!!.tag != null && galleryView!!.adapter!!.isVideo(position)) {
            toggleHUDVisibility(true)
        }
        if (galleryView!!.adapter!!.isVideo(position)) {
            shareUrl = files!![position].videoUrl
        } else {
            shareUrl = files!![position].coverUrl
        }
        upperLabel!!.text = files!![position].caption
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//    }
}
