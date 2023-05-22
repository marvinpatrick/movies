package com.movies

import androidx.paging.PagingSource
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
            mockMovieCollection.clear()
            for (x in 1..5) {
                mockMovieCollection.add(Movie(title = x.toString()))
            }
            Mockito.`when`(moviesApi.getMovies(null))
                .thenReturn(Response.success(mockMovieCollection))

            val paginatedData = moviesPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )

            Assert.assertEquals(
                PagingSource.LoadResult.Page(
                    data = mockMovieCollection,
                    prevKey = null,
                    nextKey = 2
                ),
                paginatedData
            )
        }
}
