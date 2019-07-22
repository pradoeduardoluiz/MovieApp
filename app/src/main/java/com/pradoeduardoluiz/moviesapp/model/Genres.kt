package com.pradoeduardoluiz.moviesapp.model

class Genres (
    var id:Long = 0,
    var name:String = "")
{
    override fun toString() = this.name
}