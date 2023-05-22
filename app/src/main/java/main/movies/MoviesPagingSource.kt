package main.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState

class MoviesPagingSource(private val moviesApi: MoviesApi) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val moviesList = moviesApi.getMovies().body() ?: arrayListOf()

            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (moviesList.isNotEmpty()) page + 1 else null

            LoadResult.Page(
                data = moviesList,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null
}