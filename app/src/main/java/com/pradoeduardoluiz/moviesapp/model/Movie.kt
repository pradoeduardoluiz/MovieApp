package com.pradoeduardoluiz.moviesapp.model

import com.google.gson.annotations.SerializedName


class Movie(

    @SerializedName("id")
    var id:Long = 0,
    @SerializedName("title")
    var title:String = "",
    @SerializedName("poster_path")
    var posterPath: String = "",
    @SerializedName("adult")
    var adult:Boolean = false,
    @SerializedName("overview")
    var overview: String = "",
    @SerializedName("release_date")
    var releaseDate: String = "",
    @SerializedName("genres")
    var genres: List<Genres> ?= null,
    @SerializedName("original_title")
    var originalTitle:String = "",
    @SerializedName("original_language")
    var originalLanguage:String = "",
    @SerializedName("backdrop_path")
    var backdropPath: String = "",
    @SerializedName("popularity")
    var popularity:Float = 0.0f,
    @SerializedName("vote_count")
    var voteCount: Int = 0,
    @SerializedName("vide")
    var video:Boolean = false,
    @SerializedName("vote_average")
    var voteAverage: Float = 0.0f,
    @SerializedName("runtime")
    var runTime:Int = 0,
    @SerializedName("budget")
    var budget:Float = 0.0f,
    @SerializedName("revenue")
    var revenue:Float = 0.0f

) {
    override fun toString() = title
}