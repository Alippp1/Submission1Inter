package com.example.submission1inter.model

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1inter.R
import com.example.submission1inter.ValidasiLoginViewModel
import com.example.submission1inter.akun.login.LoginActivity
import com.example.submission1inter.databinding.ActivityListStoryBinding
import com.example.submission1inter.databinding.ActivityUploadBinding
import com.example.submission1inter.upload.UploadActivity


class ListStoryActivity : AppCompatActivity() {

    private lateinit var validasiLoginViewModel: ValidasiLoginViewModel
    private val getStoryViewModel : GetStoryViewModel by viewModels()

    private lateinit var binding: ActivityListStoryBinding

    private var adapter : ListStoryAdapter?=null
    private var layoutManager: LinearLayoutManager?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validasiLoginViewModel = obtainViewModel(this)
        adapter = ListStoryAdapter()
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)

        //validasi login, get token
        validasiLoginViewModel.getToken().observe(this){
            println("soklah $it")
            getStoryViewModel.getStory(it,this@ListStoryActivity)
        }

        //view story
        getStoryViewModel.getResponse().observe(this){
            if (it != null){
                adapter?.setListStory(it)
            }
        }
        binding.rvNotes.adapter = adapter
        binding.rvNotes.layoutManager = layoutManager

        //intent detail story
        binding.rvNotes.adapter = adapter
        adapter!!.setProfil(object : ListStoryAdapter.Profil {
            override fun onItemClicked(data: ListStoryItem) {
                val intent = Intent(this@ListStoryActivity, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.Extra,data.id)
                startActivity(intent)
            }
        })


        binding.addStor.setOnClickListener{
            val intent = Intent(this@ListStoryActivity, ActivityUploadBinding::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem): Boolean{
        return when (item.itemId) {
            R.id.logout ->{
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                return true
            }
            else -> true
        }
    }
    private fun obtainViewModel(activity: AppCompatActivity) : ValidasiLoginViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory)[ValidasiLoginViewModel::class.java]
    }
}