package com.example.submission1inter.upload

import com.google.gson.annotations.SerializedName

data class UploadStoryResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
