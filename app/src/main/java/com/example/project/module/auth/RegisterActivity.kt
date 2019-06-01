package com.example.project.module.auth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.project.R
import com.example.project.module.user.AuthData
import com.example.project.module.user.dashboard.DashboardActivity
import com.example.project.module.user.User
import com.google.firebase.firestore.FirebaseFirestore
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {
        registerBtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()
            val surname = surnameEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "empty email or password", Toast.LENGTH_SHORT).show()
            } else {
                register(email, password, name, surname)
            }
        }
        goLoginBtn.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun register(email: String, password: String, name: String, surname: String) {
        AuthData.auth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val id = AuthData.auth!!.currentUser!!.uid
                    val user = User()
                    user.name = name

                    user.surname = surname
                    user.email = email

                    val database = FirebaseFirestore.getInstance()

                    database.collection("users")
                        .document(id)
                        .set(user)
                        .addOnSuccessListener {
                            val intent = Intent(applicationContext, DashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Alerter.create(this)
                                .setTitle("Error")
                                .setBackgroundColorRes(R.color.error)
                                .setText("User creation failed")
                                .show()
                        }
                } else {
                    Alerter.create(this)
                        .setTitle("Error")
                        .setBackgroundColorRes(R.color.error)
                        .setText("User creation failed")
                        .show()
                }
            }
    }
}
