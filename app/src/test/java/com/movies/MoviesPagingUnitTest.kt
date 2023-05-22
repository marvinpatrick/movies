package com.movies

import androidx.paging.PagingSource
import app.AppConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import main.movies.Movie
import main.movies.MoviesApi
import main.movies.MoviesPagingSource
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MoviesPagingUnitTest {

    @Mock
    lateinit var moviesApi: MoviesApi
    private lateinit var moviesPagingSource: MoviesPagingSource
    private val mockMovieCollection = arrayListOf<Movie>()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(MoviesApi::class.java)
        moviesPagingSource = MoviesPagingSource(moviesApi = moviesApi)
    }

    @Test
    fun when_there_is_a_small_dataset_all_movies_should_be_present() =
        runBlocking {
            //Setup
            mockMovieCollection.clear()
            for (x in 1..5) {
                mockMovieCollection.add(Movie(title = x.toString()))
            }
            Mockito.`when`(moviesApi.getMovies(null))
                .thenReturn(Response.success(mockMovieCollection))

            //Trigger
            val paginatedData = moviesPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = AppConstants.PAGE_SIZE,
                    placeholdersEnabled = false
                )
            )

            //Assertion
            Assert.assertEquals(
                PagingSource.LoadResult.Page(
                    data = mockMovieCollection,
                    prevKey = null,
                    nextKey = 2
                ),
                paginatedData
            )
        }

    @Test
    fun when_the_user_filters_by_genre_only_that_genre_should_be_fetched() =
        runBlocking {
            //Setup
            mockMovieCollection.clear()
            val selectedGenre = "Action"
            val otherGenre = "Comedy"
            moviesPagingSource = MoviesPagingSource(moviesApi = moviesApi, genre = selectedGenre)

            for (x in 1..2) {
                mockMovieCollection.add(
                    Movie(
                        title = x.toString(),
                        genres = arrayListOf(selectedGenre)
                    )
                )
            }
            for (x in 3..4) {
                mockMovieCollection.add(
                    Movie(
                        title = x.toString(),
                        genres = arrayListOf(otherGenre)
                    )
                )
            }

            val actionMovies = arrayListOf<Movie>()
            actionMovies.addAll(mockMovieCollection.subList(0, 1))
            val comedyMovies = arrayListOf<Movie>()
            comedyMovies.addAll(mockMovieCollection.subList(2, 4))
            Mockito.`when`(moviesApi.getMovies(selectedGenre))
                .thenReturn(Response.success(actionMovies))


            //Trigger
            val paginatedData = moviesPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = AppConstants.PAGE_SIZE,
                    placeholdersEnabled = false
                )
            )

            //Assertion
            Assert.assertEquals(
                PagingSource.LoadResult.Page(
                    data = actionMovies,
                    prevKey = null,
                    nextKey = 2
                ),
                paginatedData
            )
            Assert.assertNotEquals(
                PagingSource.LoadResult.Page(
                    data = comedyMovies,
                    prevKey = null,
                    nextKey = 2
                ),
                paginatedData
            )
        }
}
