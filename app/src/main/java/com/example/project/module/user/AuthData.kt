package com.example.project.module.user

import com.google.firebase.auth.FirebaseAuth

class AuthData {

    companion object {
        @JvmStatic
        var _id: String = ""
        var name: String = ""
        var surname: String = ""
        var email: String = ""
        var auth: FirebaseAuth? = null
    }

}