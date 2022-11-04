package com.example.submission1inter.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submission1inter.data.repository.GetStoryRepository

class GetStoryViewModel : ViewModel() {

    private val getStoryRepository = GetStoryRepository()

    fun getStory(token:String,context : Context){
      getStoryRepository.getStory(token, context)
    }

    fun getResponse(): LiveData<List<ListStoryItem>> = getStoryRepository.getResponse()
}