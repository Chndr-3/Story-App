package com.example.storyapp.view.map

import android.util.Log
import androidx.lifecycle.*
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.datastore.UserPreference
import com.example.storyapp.model.ListStoryItem
import com.example.storyapp.model.LoginResult
import com.example.storyapp.model.StoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val pref: UserPreference) : ViewModel() {
    private val _userStories = MutableLiveData<List<ListStoryItem>>()
    val userStories: LiveData<List<ListStoryItem>> = _userStories

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    fun getStories(auth: String) {
        val client = ApiConfig().getApiService().getStoriesLocation(auth)
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userStories.value = responseBody.listStory
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}