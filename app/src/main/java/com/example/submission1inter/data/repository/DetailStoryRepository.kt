package com.example.submission1inter.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.submission1inter.model.DetailStoryApiClient
import com.example.submission1inter.model.Story
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailStoryRepository {

    private val detailStoryApiClient = DetailStoryApiClient()
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    fun getDetailStory(token:String, id:String, context: Context){
        executorService.execute { detailStoryApiClient.getDetail(token, id, context) }
    }

    fun getDetailResponse(): LiveData<Story?> = detailStoryApiClient.detail

}