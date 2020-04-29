package com.example.diagnal.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diagnal.common.ui.ViewState
import com.example.diagnal.data.movie.Movie
import com.example.diagnal.data.movie.MovieListResponseModel


class MovieListViewModel(private val movieListRepository: MovieListRepository) : ViewModel() {
    val viewState = MutableLiveData<ViewState>()
    lateinit var movieListResponseModel: MovieListResponseModel
    val movieList = MutableLiveData<MutableList<Movie>>()
    private var preSearchMoviesList: MutableList<Movie> = mutableListOf()

    //Set this flag to true while search feature is being used
    var isSearching: Boolean = false


    /**
     * This function is used to load the initial list of movies from the repository
     */
    fun getInitialMoviesList() {
        viewState.value = ViewState.LOADING
        movieListRepository.getMovieList(1)?.apply {
            movieListResponseModel = this
            movieList.value = this.page.contentItems.movieList as MutableList<Movie>
        }
        viewState.value = ViewState.LOADED
    }

    /**
     * This function is used to retrieve a specific page from the repository
     */
    fun getMovieListPage(pageNumber: Int){
       movieListRepository.getMovieList(pageNumber).let {
           it?.page?.contentItems?.movieList?.let { it1 -> movieList.value?.addAll(it1) }
       }
        movieList.value = movieList.value
    }

    /**
     * This function is used to search through the movies list.
     * The retrieved data is updated to the [MutableLiveData] object [movieList]
     */
    fun searchMovieList(query: String?) {
        //if query is  null, reset list to initial value
        if (query == null) {
            movieList.value = preSearchMoviesList
        }else {
            movieList.value = preSearchMoviesList.filter {
                it.name.contains(query, ignoreCase = true)
            } as MutableList<Movie>
        }
        movieList.value = movieList.value
    }

    /**
     * Convenience function to set value of [isSearching] flag.
     * if [boolean] is true, it saves a copy of the list of movies from [movieList],
     * when false, the the list is restored
     */
    fun setIsSearching(boolean: Boolean) {
        if (boolean) {
            if (!isSearching)
                movieList.value?.let {
                    preSearchMoviesList = it
                }
        } else {
            movieList.value = preSearchMoviesList
        }
        isSearching = boolean
    }
}

/**
 * Factory class to provide an instance of [MovieListViewModel]
 */
@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory(private val movieListRepository: MovieListRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieListViewModel(movieListRepository) as T
    }

}