package com.example.submission1inter.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.submission1inter.ValidasiLogin
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
class ValidasiLoginRepository (application: Application){

    private val validasiLogin = ValidasiLogin(application)
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    fun setLogin(isLogin:Boolean){
        executorService.execute { validasiLogin.setLogin(isLogin) }
    }

    fun cekLogin(): LiveData<Boolean> = validasiLogin.cekLogin()

    fun setToken(token:String){
        executorService.execute { validasiLogin.setToken(token) }
    }

    fun getToken(): LiveData<String> = validasiLogin.getToken()

    fun logout(token:String){
        executorService.execute { validasiLogin.logout(token) }
    }
}

