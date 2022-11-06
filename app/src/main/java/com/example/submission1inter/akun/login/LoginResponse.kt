package com.example.submission1inter.akun.login

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class LoginResult(
    @SerializedName("name")
    var name: String? = null,

    @SerializedName("userId")
    var userId: String? = null,

    @SerializedName("token")
    var token: String? = null,
)