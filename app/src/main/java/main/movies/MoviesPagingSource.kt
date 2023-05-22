package main.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.AppConstants

class MoviesPagingSource(private val moviesApi: MoviesApi, private val genre: String? = null) :
    PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val moviesList = getData(startIndex = getStartIndex(page))

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

    private suspend fun getData(startIndex: Int? = null): ArrayList<Movie> {
        return moviesApi.getMovies(genre = genre, from = startIndex).body()
            ?: arrayListOf()
    }

    private fun getStartIndex(page: Int): Int? {
        return if (page == 1) {
            null
        } else {
            (page - 1) * AppConstants.PAGE_SIZE
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null
}