package com.example.storyapp.view.authentication

import androidx.lifecycle.*
import com.example.storyapp.datastore.UserPreference
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.model.LoginResult
import com.example.storyapp.model.ResponseResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel(private val pref: UserPreference) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: LoginResult) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
    fun signIn(email: String, password: String) {
        _isSuccess.value = false
        val service = ApiConfig().getApiService().postLogin(email, password)
        service.enqueue(object : Callback<ResponseResult> {
            override fun onResponse(
                call: Call<ResponseResult>,
                response: Response<ResponseResult>
            ) {
                val responseBody = response.body()
                val errorMessage = response.errorBody()?.string()?.let{ JSONObject(it) }
                if (response.isSuccessful) {
                    if (responseBody != null && !responseBody.error) {
                        _message.value = "Login Success"
                        _isSuccess.value = true
                        _loginResult.value = responseBody.loginResult
                    }
                } else {
                    _message.value = errorMessage?.getString("message")
                    _isSuccess.value = true
                }
            }

            override fun onFailure(call: Call<ResponseResult>, t: Throwable) {
                _message.value = t.message
                _isSuccess.value = true
            }
        })
    }

}