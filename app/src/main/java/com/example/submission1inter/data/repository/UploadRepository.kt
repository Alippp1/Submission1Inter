package com.example.submission1inter.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.submission1inter.upload.UploadApiClient
import com.example.submission1inter.upload.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UploadRepository {

    private val uploadApiClient = UploadApiClient()
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    fun getUpload(imageMultipart: MultipartBody.Part, description:RequestBody,token:String, context: Context){
        executorService.execute { uploadApiClient.getUpload(imageMultipart, description,token ,context) }
    }

    fun getUploadResponse(): LiveData<UploadStoryResponse> = uploadApiClient.upload

    fun getUploadResult(): LiveData<Boolean> = uploadApiClient.isUploadSuccess
}