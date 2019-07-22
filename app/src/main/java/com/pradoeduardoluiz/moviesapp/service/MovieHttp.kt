package com.pradoeduardoluiz.moviesapp.service

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.pradoeduardoluiz.moviesapp.model.Movie
import com.pradoeduardoluiz.moviesapp.model.Result
import com.pradoeduardoluiz.moviesapp.util.API_GET_MOVIES
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

object MovieHttp {

    @Throws(IOException::class)

    fun hasConection(context:Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun loadMoviesGson():List<Movie>?{

        val client = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(API_GET_MOVIES)
            .build()

        try{
            val response = client.newCall(request).execute()
            val json = response.body()?.string()
            val gson = Gson()
            val results = gson.fromJson<Result>(json, Result::class.java)
            return results.movies
        }catch (e:Exception){
            e.printStackTrace()
        }
        return null
    }
}