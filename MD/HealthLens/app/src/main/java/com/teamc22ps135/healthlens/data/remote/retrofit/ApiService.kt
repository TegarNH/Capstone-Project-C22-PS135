package com.teamc22ps135.healthlens.data.remote.retrofit

import com.teamc22ps135.healthlens.data.remote.response.ResultResponse
import com.teamc22ps135.healthlens.data.remote.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("/upload")
    fun uploadPicture(
        @Part file: MultipartBody.Part,
        @Part("typeDetection") typeDetection: RequestBody
    ): Call<UploadResponse>

    @GET("/result")
    fun getResult(
        @Query("id") id: String,
        @Query("typeDetection") typeDetection: String
    ): Call<ResultResponse>
}