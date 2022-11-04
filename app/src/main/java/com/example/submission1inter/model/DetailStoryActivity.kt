package com.example.submission1inter.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.submission1inter.ValidasiLoginViewModel
import com.example.submission1inter.databinding.ActivityDetailStoryBinding
import com.example.submission1inter.databinding.ActivityListStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var validasiLoginViewModel: ValidasiLoginViewModel
    private val getDetailStoryViewModel : DetailStoryViewModel by viewModels()

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra(Extra)

        validasiLoginViewModel = obtainViewModel(this)

        validasiLoginViewModel.getToken().observe(this){
            if (id != null) {
                getDetailStoryViewModel.getDetailStory(it,id,this@DetailStoryActivity)
            }
        }

        getDetailStoryViewModel.getDetailResponse().observe(this){
            if (it!= null){
                Glide.with(this)
                    .load(it.photoUrl)
                    .into(binding.imgItemPhoto)
                binding.tvItemName.text = it.name
                binding.tvItemDescription.text = it.description
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity) : ValidasiLoginViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory)[ValidasiLoginViewModel::class.java]
    }

    companion object{
        const val Extra = "extra"
    }
}