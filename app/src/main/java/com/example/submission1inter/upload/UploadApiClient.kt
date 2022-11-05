package com.example.submission1inter.upload

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submission1inter.akun.login.LoginResponse
import com.example.submission1inter.data.api.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class UploadApiClient {
    private var _upload = MutableLiveData<UploadStoryResponse>()
    val upload : LiveData<UploadStoryResponse> = _upload

    private var _isUploadSuccess = MutableLiveData<Boolean>()
    val isUploadSuccess : LiveData<Boolean> = _isUploadSuccess

    fun getUpload(imageMultipart:MultipartBody.Part, description:RequestBody, token:String, context: Context){
        val client = ApiConfig.getApiService().upload(imageMultipart, description,"bearer $token" )
        client.enqueue(object : retrofit2.Callback<UploadStoryResponse> {
            override fun onResponse(
                call: retrofit2.Call<UploadStoryResponse>,
                response: Response<UploadStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null){
                        val uploadStoryResponse = UploadStoryResponse(
                            responseBody.error,
                            responseBody.message
                        )
                        _upload.value = uploadStoryResponse
                        _isUploadSuccess.value = true
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<UploadStoryResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}