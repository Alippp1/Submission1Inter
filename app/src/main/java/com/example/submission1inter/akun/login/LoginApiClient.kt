package com.example.submission1inter.akun.login

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submission1inter.data.api.ApiConfig
import retrofit2.Response

class LoginApiClient {
    private var _login = MutableLiveData<LoginResponse>()
    val login : LiveData<LoginResponse> = _login

    private var _isLoginSuccess = MutableLiveData<Boolean>()
    val isLoginSuccess : LiveData<Boolean> = _isLoginSuccess

    fun getLogin(email:String, password:String, context: Context){
        val client = ApiConfig.getApiService().UserLogin(email, password)
        client.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null){
                       val loginResponse = LoginResponse(
                           responseBody.loginResult,
                           responseBody.error,
                           responseBody.message
                       )
                        _login.value = loginResponse
                        _isLoginSuccess.value = true
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}