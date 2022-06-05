package com.teamc22ps135.healthlens.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamc22ps135.healthlens.data.remote.response.ResultResponse
import com.teamc22ps135.healthlens.data.remote.retrofit.ApiConfig
import com.teamc22ps135.healthlens.helper.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    private val _isFailed = MutableLiveData<Event<Boolean>>()
    val isFailed: LiveData<Event<Boolean>> = _isFailed

    private val _resultDetection = MutableLiveData<ResultResponse>()
    val resultDetection: LiveData<ResultResponse> = _resultDetection

    fun uploadPhoto(
        idDetection: String?,
    ) {
        _isLoading.value = Event(true)
        val service =
            ApiConfig.getApiService().getResult(idDetection)
        service.enqueue(object : Callback<ResultResponse> {
            override fun onResponse(
                call: Call<ResultResponse>,
                response: Response<ResultResponse>
            ) {
                _isLoading.value = Event(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _resultDetection.value = response.body()
                    }
                } else {
                    _isFailed.value = Event(true)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                _isLoading.value = Event(false)
                _isFailed.value = Event(true)
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "ResultViewModel"
    }
}