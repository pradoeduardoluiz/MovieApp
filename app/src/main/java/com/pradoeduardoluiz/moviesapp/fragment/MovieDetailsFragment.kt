package com.pradoeduardoluiz.moviesapp.fragment


import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.pradoeduardoluiz.moviesapp.R
import com.pradoeduardoluiz.moviesapp.model.Movie
import com.pradoeduardoluiz.moviesapp.service.MovieHttp
import com.pradoeduardoluiz.moviesapp.util.BACKDROP_URL
import com.pradoeduardoluiz.moviesapp.util.POSTER_URL
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.coroutines.*
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class MovieDetailsFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    private var downloadJob: Job?= null
    private var movie:Movie?= null

    companion object {

        const val TAG_MOVIE_DETAILS = "tagMovieDetails"

        fun newInstance(movie: Movie): MovieDetailsFragment {
            val args = Bundle()
            args.putLong("idMovie", movie.id)

            val fragment = MovieDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val args = arguments
        val idMovie = args!!.getLong("idMovie", 0)

        if (movie != null) {
            showProgress(false)
        } else {
            if (downloadJob == null) {
                if (MovieHttp.hasConection(requireContext())) {
                    startDownloadJson(idMovie)
                } else {
                    progressBar.visibility = View.GONE
                    txtMessageDetails.setText(R.string.error_no_connection)
                }
            }else if (downloadJob?.isActive == true){
                showProgress(true)
            }
        }

    }

    private fun showProgress(show: Boolean) {
        if (show) {
            txtMessageDetails.setText(R.string.message_progress)
        }
        txtMessageDetails.visibility = if (show) View.VISIBLE else View.GONE
        progressBarDetails.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun startDownloadJson(id:Long) {

        downloadJob = launch {
            showProgress(true)
            val movieTask = withContext(Dispatchers.IO){
                MovieHttp.getMovieGson(id)
            }

            updateUI(movieTask?: Movie())
            showProgress(false)
        }
    }

    private fun updateUI(movie: Movie){
        txtTitle.text = movie.title
        rbVoteAvarage.rating = movie.voteAverage
        txtRating.text = String.format("(%.${1}f)", movie.voteAverage)
        txtReleaseDate.text = movie.releaseDate
        txtGenero.text = "Generos: " + movie.genres.toString()

        txtRunTime.text = String.format("%s %d %s", "Tempo de Filme: ", movie.runTime, "minutos")
        txtBudget.text = String.format("%s %.2f", "Total de despesas: ", movie.budget)
        txtRevenue.text = String.format("%s %.2f", "Receita: ", movie.revenue)
        txtOverview.text = movie.overview

        Glide.with(requireContext()).load(BACKDROP_URL + movie.backdropPath).into(imgBackdropPath)
        downloadJob = null
    }
}
