package main.movies

import retrofit2.Response
import retrofit2.http.GET

interface MoviesApi {
    @GET("genres")
    suspend fun getGenres(): Response<List<List<Any>>>

    @GET("movies")
    suspend fun getMovies(): Response<ArrayList<Movie>>
}