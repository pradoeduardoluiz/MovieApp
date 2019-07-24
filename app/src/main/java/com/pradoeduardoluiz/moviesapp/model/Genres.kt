package com.pradoeduardoluiz.moviesapp.model

import com.google.gson.annotations.SerializedName

class Genres (

    @SerializedName("id")
    var id:Int = 0,
    @SerializedName("name")
    var name:String = "")
{
    override fun toString() = this.name
}