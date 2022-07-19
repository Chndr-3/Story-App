package com.example.storyapp.view.authentication


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.model.FileUploadResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun signUp(name: String, password: String, email: String) {
        _isSuccess.value = false
        val service = ApiConfig().getApiService().postRegister(name, password, email)
        service.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                val responseBody = response.body()
                val errorMessage = response.errorBody()?.string()?.let{ JSONObject(it) }
                if (response.isSuccessful) {
                    if (responseBody != null && !responseBody.error) {
                        _message.value = errorMessage?.getString("message")
                        _isSuccess.value = true
                    }
                } else {
                    _message.value = errorMessage?.getString("message")
                    _isSuccess.value = true
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                _message.value = t.message
                _isSuccess.value = true
            }
        })
    }
}