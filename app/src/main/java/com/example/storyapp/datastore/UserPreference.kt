package com.example.storyapp.datastore

import android.annotation.SuppressLint
import android.content.Context
import com.example.storyapp.model.LoginResult


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreference(val context: Context) {

    fun getUser(): Flow<LoginResult> {
        return context.dataStore.data.map { preferences ->
            LoginResult(
                preferences[NAME] ?: "",
                preferences[USER_ID] ?: "",
                preferences[TOKEN] ?: "",
            )
        }
    }

    suspend fun saveUser(user: LoginResult) {
        context.dataStore.edit { preferences ->
            preferences[NAME] = user.name
            preferences[TOKEN] = user.token
            preferences[USER_ID] = user.userId

        }
    }
    suspend fun getToken(): String {
        return context.dataStore.data.first()[TOKEN]!!
    }


    suspend fun logout() {
       context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: UserPreference? = null
        private val NAME = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")
        private val USER_ID = stringPreferencesKey("user_id")

        fun getInstance(context: Context): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(context)
                INSTANCE = instance
                instance
            }
        }
    }
}