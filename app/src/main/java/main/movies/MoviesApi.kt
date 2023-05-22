package main.movies

import app.AppConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("genres")
    suspend fun getGenres(): Response<List<List<Any>>>

    @GET("movies")
    suspend fun getMovies(
        @Query("genre") genre: String? = null,
        @Query("limit") limit: Int? = AppConstants.PAGE_SIZE,
        @Query("from") from: Int? = null
    ): Response<ArrayList<Movie>>
}