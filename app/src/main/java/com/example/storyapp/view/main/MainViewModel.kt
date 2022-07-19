package com.example.storyapp.view.main

import androidx.lifecycle.*
import com.example.storyapp.datastore.UserPreference
import com.example.storyapp.model.*
import kotlinx.coroutines.launch


class MainViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}