package com.example.submission1inter.model

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1inter.*
import com.example.submission1inter.akun.login.LoginActivity
import com.example.submission1inter.databinding.ActivityListStoryBinding
import com.example.submission1inter.tema.TemaActivity
import com.example.submission1inter.tema.TemaPreferencesViewModel
import com.example.submission1inter.tema.TemaSettingPreferences
import com.example.submission1inter.tema.TemaViewModelFactory
import com.example.submission1inter.upload.UploadActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ListStoryActivity : AppCompatActivity() {

    private lateinit var validasiLoginViewModel: ValidasiLoginViewModel
//
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
            validasiLoginViewModel.getToken().observe(this) {
                println("soklah $it")
                getStoryViewModel.getStory(it, this@ListStoryActivity)
            }

        //view story
        binding.progressBar.visibility = View.VISIBLE
        getStoryViewModel.getResponse().observe(this) {
            if (it != null) {
                adapter?.setListStory(it)
                binding.progressBar.hide()
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


        playAnimation()
        binding.addStor.setOnClickListener {
            val i = Intent(this@ListStoryActivity, UploadActivity::class.java)
            startActivity(i)
        }
        Theme()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem): Boolean{
        return when (item.itemId) {
            R.id.logout ->{
                validasiLoginViewModel.logout(token = String())
                val intent = Intent(this, LoginActivity::class.java)
                finish()
                startActivity(intent)
                Toast.makeText(this@ListStoryActivity, "Berhasil Logout", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.switheme ->{
                val i = Intent(this, TemaActivity::class.java)
                startActivity(i)
                return true
            }
            else -> true
        }
    }

    private  fun playAnimation(){

        val add = ObjectAnimator.ofFloat(binding.addStor, View.ALPHA, 1f).setDuration(1000)
        val list = ObjectAnimator.ofFloat(binding.rvNotes, View.ALPHA, 1f).setDuration(1000)
        AnimatorSet().apply {
            playTogether(add, list)
            start()
        }
    }

    fun Theme(){
        val pref = TemaSettingPreferences.getInstance(dataStore)
        val preferencesViewModel = ViewModelProvider(this, TemaViewModelFactory(pref)).get(
            TemaPreferencesViewModel::class.java
        )
        preferencesViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
    private fun obtainViewModel(activity: AppCompatActivity) : ValidasiLoginViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory)[ValidasiLoginViewModel::class.java]
    }
}