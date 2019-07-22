package com.pradoeduardoluiz.moviesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pradoeduardoluiz.moviesapp.R
import com.pradoeduardoluiz.moviesapp.model.Movie
import com.pradoeduardoluiz.moviesapp.util.POSTER_URL
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter (
    private var movies: List<Movie>,
    private val onItemClick: (Movie) -> Unit
):RecyclerView.Adapter<MovieAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        val viewHolder = ViewHolder(layoutInflater)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val movie = movies[position]
        holder.bind(movie, onItemClick)
    }

    fun loadItems(movies: List<Movie>){
        this.movies = movies
    }


    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {


        var imgMovie:ImageView = itemView.imgMovie
        var txtTitle:TextView = itemView.txtTitle
        var rbVoteAvarage:RatingBar = itemView.rbVoteAvarage
        var txtRating:TextView = itemView.txtRating
        var txtReleaseDate:TextView = itemView.txtReleaseDate


        fun bind(movie: Movie, onItemClick: (Movie) -> Unit) = with (itemView){

            movie?.let {

                txtTitle.text = movie.title
                rbVoteAvarage.rating = movie.voteAverage
                txtRating.text = String.format("(%.${1}f)", movie.voteAverage)
                txtReleaseDate.text = movie.releaseDate
                Glide.with(context).load(POSTER_URL + movie.posterPath).into(imgMovie)

                setOnClickListener(){
                    onItemClick(movie)
                }
            }

        }

    }

}