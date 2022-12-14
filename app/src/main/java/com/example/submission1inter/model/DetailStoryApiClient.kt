package com.example.submission1inter.model

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submission1inter.data.api.ApiConfig
import retrofit2.Response

class DetailStoryApiClient{
    private var _detail = MutableLiveData<Story?>()
    val detail : LiveData<Story?> = _detail

    private var _isDetailSuccess = MutableLiveData<Boolean>()
    val isDetailSuccess : LiveData<Boolean> = _isDetailSuccess

    fun getDetail(token:String, id:String, context: Context){
        val client = ApiConfig.getApiService().Getstories("bearer $token", id)
        client.enqueue(object : retrofit2.Callback<DetailStoryResponse> {
            override fun onResponse(
                call: retrofit2.Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null){

                        _detail.value = responseBody.story
                        _isDetailSuccess.value = true
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<DetailStoryResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}