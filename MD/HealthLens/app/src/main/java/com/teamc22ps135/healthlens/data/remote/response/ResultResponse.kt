package com.teamc22ps135.healthlens.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResultResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("listStory")
    val listStory: List<Story>
//    @field:SerializedName("resultDetection")
//    val resultDetection: String,
//    @field:SerializedName("recommendationList")
//    val recommendationList: RecommendationList,
//    @field:SerializedName("productList")
//    val productList: ProductList
)

data class Story(
    @field:SerializedName("id")
    val id: String? = null,
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("description")
    val description: String? = null,
)

data class RecommendationList(
    @field:SerializedName("recommendation")
    val recommendation: String
)

data class ProductList(
    @field:SerializedName("photo")
    val photo: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("linkProduct")
    val linkProduct: String
)
