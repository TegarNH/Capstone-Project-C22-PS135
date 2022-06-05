package com.teamc22ps135.healthlens.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResultResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("resultDetection")
    val resultDetection: String,
    @field:SerializedName("rekomendationList")
    val recommendationList: ArrayList<String>,
    @field:SerializedName("productList")
    val productList: List<ProductList>
)

data class ProductList(
    @field:SerializedName("photo")
    val photo: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("linkProduct")
    val linkProduct: String
)
