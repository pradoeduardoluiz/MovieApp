package com.pradoeduardoluiz.moviesapp.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.pradoeduardoluiz.moviesapp.R
import com.pradoeduardoluiz.moviesapp.adapter.MovieAdapter
import com.pradoeduardoluiz.moviesapp.model.Movie
import com.pradoeduardoluiz.moviesapp.service.MovieHttp
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MovieListFragment : Fragment(), CoroutineScope {

    private lateinit var job:Job
    private var downloadJob:Job?= null
    private var movies = mutableListOf<Movie>()
    private var adapter = MovieAdapter(movies, this::onItemClick)
    private var searcView:SearchView? = null
    private var page:Int = 0
    private var totalPage = 99
    private var loading = false

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
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    companion object {

        const val TAG_LIST_MOVIE = "tagListBook"

        fun newInstance(): MovieListFragment {
            return MovieListFragment()

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvMovie.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        rvMovie.layoutManager = layoutManager


        rvMovie.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = rvMovie.layoutManager as LinearLayoutManager?

                if (!loading && linearLayoutManager!!.itemCount <=
                    linearLayoutManager.findLastVisibleItemPosition() + 2){
                    loading = true
                    nextPageOfMovies()
                }
            }
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (movies.isNotEmpty()) {
            showProgress(false)
        } else {
            if (downloadJob == null) {
                if (MovieHttp.hasConection(requireContext())) {
                    startDownloadJson()
                } else {
                    progressBar.visibility = View.GONE
                    txtMessage.setText(R.string.error_no_connection)
                }
            }else if (downloadJob?.isActive == true){
                showProgress(true)
            }
        }
    }

    fun nextPageOfMovies(){
        page++

        if (page <= totalPage){

            if (downloadJob == null) {
                if (MovieHttp.hasConection(requireContext())) {
                    startDownloadJson()
                } else {
                    progressBar.visibility = View.GONE
                    txtMessage.setText(R.string.error_no_connection)
                }
            }else if (downloadJob?.isActive == true){
                showProgress(true)
            }

        }
    }


    private fun onItemClick(movie: Movie) {

        val fragment = MovieDetailsFragment.newInstance(movie)
        fragmentManager!!.beginTransaction()
            .replace(R.id.fragmentList, fragment, MovieDetailsFragment.TAG_MOVIE_DETAILS)
            .addToBackStack(TAG_LIST_MOVIE)
            .commit()
        searcView?.setOnQueryTextListener(null)
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            txtMessage.setText(R.string.message_progress)
        }
        txtMessage.visibility = if (show) View.VISIBLE else View.GONE
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun startDownloadJson() {

        downloadJob = launch {
            showProgress(true)
            val responseTask = withContext(Dispatchers.IO){
                MovieHttp.loadMoviesGson(page)
            }
            page = responseTask!!.page
            totalPage = responseTask!!.totalPages
            if (page <= 1){
                updateMovieList(responseTask?.movies)
            }else
            {
                addMoviesToAdapter(responseTask.movies)
            }

            showProgress(false)
        }
    }

    private fun addMoviesToAdapter(movies: List<Movie>) {
        adapter.addData(movies.toMutableList())
        loading = false
        downloadJob = null
    }

    private fun updateMovieList(result: List<Movie>?) {

        if(result != null){
            movies.clear()
            movies.addAll(result)
        }else{
            txtMessage.setText(R.string.error_load_movies)
        }
        adapter?.loadItems(movies)
        adapter?.notifyDataSetChanged()
        downloadJob = null
    }

    fun search(text:String){
        adapter.filter.filter(text)
    }
    fun clearSearch(){
        adapter.filter.filter("")
    }
}
