package com.pradoeduardoluiz.moviesapp.model

import com.google.gson.annotations.SerializedName

class Response(

    @SerializedName("results")
    var movies:List<Movie> = emptyList(),

    @SerializedName("page")
    var page:Int=0,
    @SerializedName("total_pages")
    var totalPages:Int=0
) {
}