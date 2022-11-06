package com.example.submission1inter.model

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submission1inter.data.api.ApiConfig
import retrofit2.Response

class GetStoryApiClient {
    private var _getStories = MutableLiveData<List<ListStoryItem>>()
    val getStory: LiveData<List<ListStoryItem>> = _getStories

    fun getStory(token:String,context : Context) {
        val client = ApiConfig.getApiService().stories("bearer $token")
        client.enqueue(object : retrofit2.Callback<StoryResponse> {
            override fun onResponse(
                call: retrofit2.Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _getStories.value = response.body()?.listStory

                    }
                } else {
                    Toast.makeText(context,response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<StoryResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}