package main.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.AppConstants
import kotlinx.coroutines.flow.Flow

class MoviesRepo(private val moviesApi: MoviesApi) {

    suspend fun getGenres(): List<List<Any>>? {
        return moviesApi.getGenres().body()
    }

    fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = AppConstants.PAGE_SIZE)) {
            MoviesPagingSource(moviesApi = moviesApi)
        }.flow
    }
}