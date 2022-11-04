package com.example.submission1inter.upload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.submission1inter.R
import com.example.submission1inter.databinding.ActivityListStoryBinding
import com.example.submission1inter.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}