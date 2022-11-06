package com.example.submission1inter.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.submission1inter.model.GetStoryApiClient
import com.example.submission1inter.model.ListStoryItem
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GetStoryRepository {

    private val getStoryApiClient = GetStoryApiClient()
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    fun getStory(token:String,context : Context){
        executorService.execute { getStoryApiClient.getStory(token, context) }
    }

    fun getResponse(): LiveData<List<ListStoryItem>> = getStoryApiClient.getStory

}