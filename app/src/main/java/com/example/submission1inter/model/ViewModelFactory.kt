package com.example.submission1inter.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submission1inter.ValidasiLoginViewModel

class ViewModelFactory private constructor(private val application: Application): ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile private var INSTANCE : ViewModelFactory? = null
        fun getInstance(application : Application): ViewModelFactory{
            return INSTANCE?: synchronized(this){
                val instance = ViewModelFactory(application)
                INSTANCE = instance
                return instance
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return when{
            modelClass.isAssignableFrom(ValidasiLoginViewModel::class.java) -> ValidasiLoginViewModel(application) as T
           else -> {
               throw  IllegalArgumentException("Unknown ViewModel class:" + modelClass.name)
           }
       }
    }
}