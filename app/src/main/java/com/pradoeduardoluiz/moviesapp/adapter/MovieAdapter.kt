package com.pradoeduardoluiz.moviesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pradoeduardoluiz.moviesapp.R
import com.pradoeduardoluiz.moviesapp.model.Movie
import com.pradoeduardoluiz.moviesapp.util.POSTER_URL
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter (
    private var movies: List<Movie>,
    private val onItemClick: (Movie) -> Unit
):RecyclerView.Adapter<MovieAdapter.ViewHolder>(), Filterable{

    private var moviesSearchList:List<Movie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        val viewHolder = ViewHolder(layoutInflater)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return moviesSearchList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val movie = moviesSearchList[position]
        holder.bind(movie, onItemClick)
    }

    fun loadItems(movies: List<Movie>){
        this.moviesSearchList = movies
    }

    override fun getFilter(): Filter {
        return object :Filter(){

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if(charString.isEmpty()){
                    moviesSearchList = movies
                }else{
                    val filteredList = ArrayList<Movie>()
                    for(movie in movies){
                        if(movie.title!!.toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(movie)
                        }
                    }

                    moviesSearchList = filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = moviesSearchList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, results: FilterResults?) {
                moviesSearchList = results?.values as ArrayList<Movie>
                notifyDataSetChanged()
            }
        }
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