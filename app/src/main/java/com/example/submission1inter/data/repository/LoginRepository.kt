package com.example.submission1inter.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.submission1inter.akun.login.LoginApiClient
import com.example.submission1inter.akun.login.LoginResponse
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LoginRepository {
    private val loginApiClient = LoginApiClient()
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    fun getLogin(email:String, password:String, context: Context){
        executorService.execute { loginApiClient.getLogin(email, password, context) }
    }

    fun getLoginResponse(): LiveData<LoginResponse> = loginApiClient.login

    fun getLoginResult(): LiveData<Boolean> = loginApiClient.isLoginSuccess
}