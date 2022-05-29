package com.teamc22ps135.healthlens.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamc22ps135.healthlens.data.remote.response.UploadResponse
import com.teamc22ps135.healthlens.data.remote.retrofit.ApiConfig
import com.teamc22ps135.healthlens.helper.Event
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewDetectViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    private val _isFailed = MutableLiveData<Event<Boolean>>()
    val isFailed: LiveData<Event<Boolean>> = _isFailed

    private val _isUploadSuccess = MutableLiveData<Event<Boolean>>()
    val isUploadSuccess: LiveData<Event<Boolean>> = _isUploadSuccess

    private val _idDetection = MutableLiveData<String?>()
    val idDetection: LiveData<String?> = _idDetection

    fun uploadPhoto(
        image: MultipartBody.Part,
        typeDetection: RequestBody
    ) {
        _isLoading.value = Event(true)
        val service =
            ApiConfig.getApiService().uploadPicture(image, typeDetection)
        service.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                _isLoading.value = Event(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _isUploadSuccess.value = Event(true)
                        _idDetection.value = responseBody.message
                    }
                } else {
                    _isUploadSuccess.value = Event(false)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _isLoading.value = Event(false)
                _isFailed.value = Event(true)
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "ReviewDetectViewModel"
    }
}