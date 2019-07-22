package com.pradoeduardoluiz.moviesapp.model

import com.google.gson.annotations.SerializedName

class Result(

    @SerializedName("results")
    var movies:List<Movie> = emptyList()

) {
}