package com.teamc22ps135.healthlens.data.remote.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
//    @field:SerializedName("id")
//    val id: String? = null
)
