package com.pradoeduardoluiz.moviesapp.service

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.pradoeduardoluiz.moviesapp.model.Movie
import com.pradoeduardoluiz.moviesapp.model.Response
import com.pradoeduardoluiz.moviesapp.util.API_GET_MOVIE_DETAILS
import com.pradoeduardoluiz.moviesapp.util.API_GET_MOVIES
import com.pradoeduardoluiz.moviesapp.util.API_KEY
import okhttp3.HttpUrl
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

    fun loadMoviesGson(page:Int):Response?{

        val client = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

        var urlBuilder: HttpUrl.Builder = HttpUrl.parse(API_GET_MOVIES)!!.newBuilder()
        urlBuilder.addQueryParameter("api_key", API_KEY)
        urlBuilder.addQueryParameter("language", "pt-br")
        if(page > 0){
            urlBuilder.addQueryParameter("page", page.toString())
        }

        var url = urlBuilder.build().toString()

        val request = Request.Builder()
            .url(url)
            .build()

        try{
            val response = client.newCall(request).execute()
            val json = response.body()?.string()
            val gson = Gson()
            val results = gson.fromJson<Response>(json, Response::class.java)
            return results
        }catch (e:Exception){
            e.printStackTrace()
        }
        return null
    }

    fun getMovieGson(id:Long):Movie?{

        val client = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

        var urlBuilder: HttpUrl.Builder = HttpUrl.parse("$API_GET_MOVIE_DETAILS/$id")!!.newBuilder()
        urlBuilder.addQueryParameter("api_key", API_KEY)
        urlBuilder.addQueryParameter("language", "pt-br")

        var url = urlBuilder.build().toString()

        val request = Request.Builder()
            .url(url)
            .build()

        try{
            val response = client.newCall(request).execute()
            val json = response.body()?.string()
            val gson = Gson()
            val results = gson.fromJson<Movie>(json, Movie::class.java)
            return results
        }catch (e:Exception){
            e.printStackTrace()
        }
        return null
    }
}