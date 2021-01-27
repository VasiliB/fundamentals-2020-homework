package com.android.academy.fundamentals.homework.features.movies

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.academy.fundamentals.homework.R
import com.android.academy.fundamentals.homework.data.loadMovies
import com.android.academy.fundamentals.homework.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MoviesListFragment : Fragment() {

    private var listener: MoviesListItemClickListener? = null

    private var movies: List<Movie>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MoviesListItemClickListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_movies_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.recycler_movies).apply {
            this.layoutManager = GridLayoutManager(this.context, 2)

            val adapter = MoviesListAdapter { movieData ->
                listener?.onMovieSelected(movieData)
            }

            this.adapter = adapter

            loadDataToAdapter(adapter)
        }
    }

    private fun loadDataToAdapter(adapter: MoviesListAdapter) {
        lifecycleScope.launch {
            val moviesData = movies ?: loadMovies(requireContext())

            withContext(Dispatchers.Main) {
                movies = moviesData
                adapter.submitList(moviesData)
            }
        }
    }

    override fun onDetach() {
        listener = null

        super.onDetach()
    }

    interface MoviesListItemClickListener {
        fun onMovieSelected(movie: Movie)
    }

    companion object {
        fun create() = MoviesListFragment()
    }
}