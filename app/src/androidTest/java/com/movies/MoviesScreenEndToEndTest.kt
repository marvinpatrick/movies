@file:OptIn(ExperimentalTestApi::class, ExperimentalTestApi::class)

package com.movies

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.paging.compose.collectAsLazyPagingItems
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import main.movies.MoviesRepo
import main.movies.MoviesScreen
import main.movies.MoviesViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MoviesScreenEndToEndTest {

    @get:Rule(order = 1)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var moviesRepo: MoviesRepo
    private lateinit var moviesViewModel: MoviesViewModel

    @Before
    fun setup() {
        hiltTestRule.inject()
    }

    @Test
    fun moviesScreenEndToEndTest() {
        moviesViewModel = MoviesViewModel(moviesRepo = moviesRepo)

        composeTestRule.setContent {
            MoviesScreen(
                genres = moviesViewModel.genres,
                movies = moviesViewModel.movies.value.collectAsLazyPagingItems(),
                fetchMovies = { genre -> moviesViewModel.getMovies(genre = genre) }
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(
            matcher = hasTestTag("movie-card"),
            timeoutMillis = 5000
        )
    }
}