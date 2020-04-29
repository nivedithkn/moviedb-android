package com.example.diagnal.feature.main

import android.content.Context
import android.util.Log
import com.example.diagnal.R
import com.example.diagnal.data.movie.MovieListResponseModel
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

/**
 * [MovieListRepository] is used to implement Repository pattern.
 * Acts as single source of truth for objects that subscribe to it.
 * Handles all interaction with the API
 */

class MovieListRepository(private val mContext: Context) {

    /**
     * Function retrieves list of movies from the API.
     */
    fun getMovieList(pageNumber: Int):MovieListResponseModel? {
        val json: String?
        try {
            val inputStream: InputStream = mContext.assets.open(mContext.resources.getString(R.string.json_file_with_page_number, pageNumber))
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            val gson = Gson()
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.forName("UTF-8"))
            val movieListResponseModel = json.let {
                gson.fromJson(it, MovieListResponseModel::class.java)
            }
            Log.d("MovieListRepository", movieListResponseModel.toString())
            return movieListResponseModel
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}