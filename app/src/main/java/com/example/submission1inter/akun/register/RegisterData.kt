package com.example.submission1inter.akun.register

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterData (
    var name: String? = null,
    var email: String? = null,
    var password: String? = null
)