package com.example.submission1inter.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submission1inter.akun.login.LoginResponse
import com.example.submission1inter.data.repository.DetailStoryRepository

class DetailStoryViewModel : ViewModel() {

    val getDetailStoryRepository = DetailStoryRepository()

    fun getDetailStory(token:String, id:String, context: Context){
        getDetailStoryRepository.getDetailStory(token, id,context)
    }

    fun getDetailResponse(): LiveData<Story?> = getDetailStoryRepository.getDetailResponse()

}