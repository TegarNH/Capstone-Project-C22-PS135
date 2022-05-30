package com.teamc22ps135.healthlens.data.remote.retrofit

import com.teamc22ps135.healthlens.data.remote.response.ResultResponse
import com.teamc22ps135.healthlens.data.remote.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
//    @POST("/upload")
    @POST("/v1/stories")
    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLU9Wbm82Sk5yYWhhMHBJOWIiLCJpYXQiOjE2NTM3NTQxNDB9.H49mZUKVwDkufsPOO3t9-RIoyI27kdAb63M3z_SsW80")
    fun uploadPicture(
        @Part file: MultipartBody.Part,
//        @Part("typeDetection") typeDetection: RequestBody
        @Part("description") typeDetection: RequestBody
    ): Call<UploadResponse>

//    @GET("/result")
    @GET("/v1/stories")
    fun getResult(
    @Header("Authorization") id: String,
//        @Query("id") id: String,
//        @Query("typeDetection") typeDetection: String
    ): Call<ResultResponse>
}