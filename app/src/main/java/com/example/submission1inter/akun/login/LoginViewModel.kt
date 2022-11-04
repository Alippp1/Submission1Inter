package com.example.submission1inter.akun.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submission1inter.data.repository.LoginRepository

class LoginViewModel(): ViewModel(){
    private val loginRepository = LoginRepository()

    fun getLogin(email:String, password:String, context: Context){
       loginRepository.getLogin(email, password,context)
    }

    fun getLoginResponse(): LiveData<LoginResponse> = loginRepository.getLoginResponse()

    fun getLoginResult(): LiveData<Boolean> = loginRepository.getLoginResult()
}