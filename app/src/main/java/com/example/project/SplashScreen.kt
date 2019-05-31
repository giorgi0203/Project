package com.example.project

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.project.module.auth.LoginActivity
import com.example.project.module.user.DashboardActivity
import com.example.project.module.user.User
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.concurrent.schedule

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        init()

    }

    private fun init() {
        //setup firebase auth
        User.auth = FirebaseAuth.getInstance()

        //check for auth
        if (User.auth!!.currentUser != null){
            Handler().postDelayed({
                val intent = Intent(applicationContext, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }, 1000)

        }else {
            Handler().postDelayed({
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }, 2000)
        }
        //open login activity or dashboard


    }
}
