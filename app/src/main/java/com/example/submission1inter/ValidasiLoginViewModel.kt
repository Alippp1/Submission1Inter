package com.example.submission1inter

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submission1inter.data.repository.ValidasiLoginRepository

class ValidasiLoginViewModel (application: Application): ViewModel(){
    private val validasiLoginRepository = ValidasiLoginRepository(application)

    fun setLogin(isLogin:Boolean){
       validasiLoginRepository.setLogin(isLogin)
    }

    fun cekLogin(): LiveData<Boolean> = validasiLoginRepository.cekLogin()

    fun setToken(token:String){
        validasiLoginRepository.setToken(token)
    }

    fun getToken(): LiveData<String> = validasiLoginRepository.getToken()
}