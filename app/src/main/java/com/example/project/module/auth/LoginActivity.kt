package com.example.project.module.auth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.project.R
import com.example.project.module.user.dashboard.DashboardActivity
import com.example.project.module.user.AuthData.Companion.auth
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        loginBtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Alerter.create(this)
                    .setTitle("Error")
                    .setBackgroundColorRes(R.color.error)
                    .setText("Empty Email or Password")
                    .show()
                Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT).show()
            } else {
                login(email,password)
            }
        }

        goRegisterBtn.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(email: String, password: String) {


        auth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(applicationContext, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    Alerter.create(this)
                        .setTitle("Error")
                        .setBackgroundColorRes(R.color.error)
                        .setText("Authentication failed.")
                        .show()
                }
            }

    }


}
