package com.example.submission1inter.tema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TemaViewModelFactory (private val pref: TemaSettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemaPreferencesViewModel::class.java)) {
            return TemaPreferencesViewModel(pref) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class:" + modelClass.name)
    }
}