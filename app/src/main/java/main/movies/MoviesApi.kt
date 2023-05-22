package main.movies

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("genres")
    suspend fun getGenres(): Response<List<List<Any>>>

    @GET("movies")
    suspend fun getMovies(
        @Query("genre") genre: String? = null
    ): Response<ArrayList<Movie>>
}