package com.example.project.module.user.dashboard

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.project.R
import com.example.project.module.auth.LoginActivity
import com.example.project.module.user.AuthData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        init()
    }

    private fun init() {
        val database = FirebaseFirestore.getInstance()
        database.collection("users").document(AuthData.auth!!.currentUser!!.uid).get()
            .addOnSuccessListener {
//                userName.text = it.get("name").toString()
//                userSurname.text = it.get("surname").toString()
            }
            .addOnFailureListener {
            }

        lgoAuthBtn.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            //signout user
            FirebaseAuth.getInstance().signOut()

            startActivity(intent)
        }
    }
}
