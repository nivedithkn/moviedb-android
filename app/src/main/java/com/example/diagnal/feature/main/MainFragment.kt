package com.example.diagnal.feature.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diagnal.R
import com.example.diagnal.common.extensions.hideKeyboard
import com.example.diagnal.common.extensions.showKeyboard
import com.example.diagnal.common.ui.ViewState
import com.example.diagnal.data.movie.Movie
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var listAdapter: MovieAdapter

    private var currentPage: Int = 1

    //TOTAL_PAGES is not a necessary when dealing with real world APIs
    private val TOTAL_PAGES = 3

    private val movieListRepository by lazy {
        MovieListRepository(mContext)
    }


    private val movieListViewModel by lazy {
        ViewModelProvider(this, MovieListViewModelFactory(movieListRepository)).get(
            MovieListViewModel::class.java
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        listAdapter = MovieAdapter(mContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()

        movieListViewModel.getInitialMoviesList()
    }

    /**
     * This function is used to setup different observers on to this fragment's ViewModel [MovieListViewModel]
     * and it's LiveData objects
     */
    private fun setupObservers() {
        movieListViewModel.viewState.observe(
            viewLifecycleOwner,
            Observer { viewState ->
                when (viewState ?: ViewState.LOADING) {
                    ViewState.LOADING -> progress_circular.visibility = View.VISIBLE
                    ViewState.LOADED -> progress_circular.visibility = View.GONE
                    ViewState.ERROR -> TODO()
                }
            }
        )
        movieListViewModel.movieList.observe(
            viewLifecycleOwner,
            Observer<List<Movie>> { movieList ->
                if (movieList.isEmpty()) {
                    empty_list_placeholder.visibility = View.VISIBLE
                    rv_movie_list.visibility = View.GONE
                } else {
                    empty_list_placeholder.visibility = View.GONE
                    rv_movie_list.visibility = View.VISIBLE
                }
                if (movieListViewModel.isSearching)
                    listAdapter.clear()

                listAdapter.addMovies(movieList)
            })
    }

    /**
     * This function sets up the [RecyclerView] with it's layoutManager, adapter and other properties
     */
    private fun setupRecyclerView() {

        val columns = resources.getInteger(R.integer.rv_movies_span)
        val gridLayoutManager =
            GridLayoutManager(mContext, columns, GridLayoutManager.VERTICAL, false)
        rv_movie_list.addItemDecoration(MarginItemDecoration(columns, 20))
        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.adapter = listAdapter
    }

    /**
     * This function is used to set up  the various listeners to different [View] objects in the expanded
     * layout
     */
    private fun setupListeners() {

        img_back.setOnClickListener {
            activity?.onBackPressed()
        }

        img_search.setOnClickListener {
            img_search.visibility = View.GONE
            appbar_title.visibility = View.GONE
            search_view.visibility = View.VISIBLE
            movieListViewModel.setIsSearching(true)
            search_view.requestFocus()
            if (search_view.requestFocus()) {
                search_view.showKeyboard()
            }
        }

        search_view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty() && s.length >= 3) {
                    movieListViewModel.searchMovieList(s.toString())
                }
                Log.d("MainFragmentAfter", " s= $s")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d(
                    "MainFragmentBefore",
                    " s= $s   start = $start  count = $count  after = $after"
                )
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(
                    "MainFragmentonChanged",
                    " s= $s   start = $start  count = $count  before = $before"
                )
                if (count == 2 && before > count) {
                    movieListViewModel.searchMovieList(null)
                }
            }

        })

        /**
         * [search_view] is an EditText with a right drawable. The below code sets a touch listener
         *  to the right drawable.
         */
        search_view.setOnTouchListener(OnTouchListener { v, event ->
//            val DRAWABLE_LEFT = 0
//            val DRAWABLE_TOP = 1
//            val DRAWABLE_BOTTOM = 3
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= search_view.right - search_view.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    search_view.text = null
                    img_search.visibility = View.VISIBLE
                    appbar_title.visibility = View.VISIBLE
                    search_view.visibility = View.GONE
                    movieListViewModel.setIsSearching(false)
                    search_view.hideKeyboard()
                    return@OnTouchListener true
                }
            }
            false
        })

        rv_movie_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {

                    val layoutManager = rv_movie_list.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.findLastCompletelyVisibleItemPosition() + 1
                    if (visibleItemCount == layoutManager.itemCount) {
                        if (currentPage < TOTAL_PAGES) {
                            currentPage++
                            loadNextPage()
                        }
                    }


                }
            }
        })
    }

    /**
     * This function is used to the load the next page of Movie listings. Used for pagination.
     */
    private fun loadNextPage() {
        movieListViewModel.getMovieListPage(currentPage)
    }
}