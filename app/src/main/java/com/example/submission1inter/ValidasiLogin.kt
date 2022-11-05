package com.example.submission1inter

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData

class ValidasiLogin(var context: Context) {
    private var sharedPreferences:SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null


    init {
        sharedPreferences = context.getSharedPreferences("AppKey",0)
        editor = sharedPreferences?.edit()
        editor?.apply()
    }

    fun setLogin(isLogin:Boolean){
        editor?.putBoolean("SedangLogin", isLogin)
        editor?.commit()
    }

    private val isLogin = MutableLiveData<Boolean>()

    fun cekLogin(): MutableLiveData<Boolean> {
      isLogin.value = sharedPreferences?.getBoolean("SedangLogin", false)
        return isLogin
    }

    fun setToken(token:String){
        editor?.putString("token", token)
        editor?.commit()
    }

    private val token = MutableLiveData<String>()

    fun getToken(): MutableLiveData<String> {
        token.value = sharedPreferences?.getString("token", "null")
        return token
    }

    fun logout(){
        editor?.clear()
        editor?.apply()
    }
}