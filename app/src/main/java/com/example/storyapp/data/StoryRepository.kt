package com.example.storyapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp.api.ApiService
import com.example.storyapp.database.StoryDatabase
import com.example.storyapp.model.ListStoryItem

class StoryRepository(private val context: Context, private val apiService: ApiService, private val storyDatabase: StoryDatabase) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(context, storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllQuote()
            }

        ).liveData
    }
}