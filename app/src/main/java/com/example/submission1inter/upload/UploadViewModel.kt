package com.example.submission1inter.upload

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submission1inter.data.repository.UploadRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(): ViewModel(){

    private val uploadRepository = UploadRepository()

    fun getUpload(imageMultipart: MultipartBody.Part, description: RequestBody, token:String, context: Context){
        uploadRepository.getUpload(imageMultipart, description,token,context)
    }

    fun getUploadResponse(): LiveData<UploadStoryResponse> = uploadRepository.getUploadResponse()

    fun getUploadResult(): LiveData<Boolean> = uploadRepository.getUploadResult()
}