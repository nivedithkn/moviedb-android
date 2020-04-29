package com.example.diagnal.data.movie

import com.google.gson.annotations.SerializedName


data class MovieListResponseModel(
    @SerializedName("page") val page:PageModel
)

data class PageModel(
    @SerializedName("title") val pageTitle:String,
    @SerializedName("total-content-items") val contentCount:String,
    @SerializedName("page-num") val pageNum:String,
    @SerializedName("page-size") val pageSize:String,
    @SerializedName("content-items") val contentItems:ContentItems
)

data class ContentItems (
    @SerializedName("content") var movieList:List<Movie>
)

data class Movie(
    @SerializedName("name") val name: String,
    @SerializedName("poster-image") val poster_image: String
)