package com.example.submission1inter.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1inter.ValidasiLoginViewModel
import com.example.submission1inter.databinding.ActivityDetailStoryBinding
import com.example.submission1inter.databinding.ActivityListStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var getDetailStoryViewModel : DetailStoryViewModel

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        getDetailStoryViewModel = obtainViewModel(this)
        getDetailStoryViewModel.getDetailResponse().observe(this){
            println("soklah $it")//debug
//            Toast.makeText(this@DetailStoryActivity,getDetailStoryViewModel.getDetailResponse().toString(), Toast.LENGTH_SHORT).show()
//            getDetailStoryViewModel.getDetailResponse(it,this@DetailStoryActivity)
        }
    }
//    private fun obtainViewModel(activity: AppCompatActivity) : DetailStoryViewModel {
//        val factory = ViewModelFactory.getInstance(activity.application)
//        return ViewModelProvider(activity,factory)[DetailStoryViewModel::class.java]
//    }
}