package com.example.project.module.user.dashboard


import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.View
import com.example.project.R
import com.example.project.module.auth.LoginActivity
import com.example.project.module.user.AddImageActivity
import com.example.project.module.user.AuthData
import com.example.project.module.user.dashboard.adapters.NavigationAdapter
import com.example.project.module.user.dashboard.fragments.FeedFragment
import com.example.project.module.user.dashboard.fragments.GalleryFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_image.*
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private var isMenuOpen = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        init()
        loadFragments()
    }

    @SuppressLint("RestrictedApi")
    private fun init() {
        addNewItem.visibility = View.GONE
        logautBtn.visibility = View.GONE
        val database = FirebaseFirestore.getInstance()
        database.collection("users").document(AuthData.auth!!.currentUser!!.uid).get()
            .addOnSuccessListener {
//                userName.text = it.get("name").toString()
//                userSurname.text = it.get("surname").toString()
            }
            .addOnFailureListener {
            }
        setListeners()

    }

    private fun setListeners() {
        floatingMenuBtn.setOnClickListener {
            if (isMenuOpen){
                floatingMenuBtn.setImageResource(R.drawable.baseline_add_white_18dp)
                closeFloatingMenu()
            }else {
                floatingMenuBtn.setImageResource(R.drawable.baseline_clear_white_18dp)
                openFloatingMenu()
            }

        }

        logautBtn.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            //signout user
            FirebaseAuth.getInstance().signOut()

            startActivity(intent)
        }
        addNewItem.setOnClickListener {
            val intent = Intent(applicationContext, AddImageActivity::class.java)
            startActivity(intent)
        }



        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    viewPager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    viewPager.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }



    @SuppressLint("RestrictedApi")
    private fun closeFloatingMenu() {
        isMenuOpen=false
        addNewItem.visibility = View.GONE
        logautBtn.visibility = View.GONE
    }

    @SuppressLint("RestrictedApi")
    private fun openFloatingMenu() {
        isMenuOpen=true
        addNewItem.visibility = View.VISIBLE
        logautBtn.visibility = View.VISIBLE
    }

    private fun loadFragments(){
        val items = ArrayList<Fragment>()
        items.add(FeedFragment())
        items.add(GalleryFragment())

        val adapter = NavigationAdapter(items,supportFragmentManager)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                navigation.menu.getItem(p0).isChecked = true
            }

        })
    }
}
