package com.example.project.module.user

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.project.R
import com.example.project.module.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        lgoAuthBtn.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            //signout user
            FirebaseAuth.getInstance().signOut()

            startActivity(intent)
        }
    }
}
