package com.example.diagnal.feature.main

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.diagnal.R
import com.example.diagnal.data.movie.Movie
import com.example.diagnal.databinding.ItemMovieBinding


/**
 * RecyclerView Adapter class that provides [MovieAdapter.ViewHolder] viewholders.
 *
 */
class MovieAdapter(private val mContext: Context) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private val movieList:MutableList<Movie> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        val itemView: ItemMovieBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_movie,  parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        holder.itemBinding.movie = movieList[position]
    }

    /**
     * Function to add new list of movies to the adapter.
     * This function will trigger a rebuild of the recyclerview
     */
    fun addMovies(list: List<Movie>){
        movieList.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * Function to add clear current list of movies in the adapter.
     * This function will trigger a rebuild of the recyclerview
     */
    fun clear(){
        movieList.clear()
        notifyDataSetChanged()
    }

    /**
     * ViewHolder class that is provided by this adapter
     */
    inner class ViewHolder(private val itemMovieBinding: ItemMovieBinding) : RecyclerView.ViewHolder(itemMovieBinding.root) {
        val itemBinding: ItemMovieBinding = itemMovieBinding
    }
}


class MarginItemDecoration(private val span: Int,private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {        with(outRect) {
        val position = parent.getChildAdapterPosition(view)
        if (position in 0..span) {
            top = spaceHeight
        }
        if(position%span == 0)
            right = spaceHeight

        if((position+1)%span == 0)
            left = spaceHeight
        bottom = spaceHeight*2
    }
    }
}