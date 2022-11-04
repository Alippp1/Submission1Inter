package com.example.submission1inter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.submission1inter.akun.login.LoginActivity
import com.example.submission1inter.model.ListStoryActivity
import com.example.submission1inter.model.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var validasiLoginViewModel: ValidasiLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        validasiLoginViewModel = obtainViewModel(this)
        validasiLoginViewModel.cekLogin().observe(this){
            when{
                it -> {
                    startActivity(Intent(this@MainActivity, ListStoryActivity::class.java))
                    finish()
                }else ->{
                  startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                  finish()
                }
            }
        }
    }
    private fun obtainViewModel(activity: AppCompatActivity) : ValidasiLoginViewModel{
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory)[ValidasiLoginViewModel::class.java]
    }
}