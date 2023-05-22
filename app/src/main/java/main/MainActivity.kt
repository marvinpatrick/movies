package main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import app.MoviesTheme
import dagger.hilt.android.AndroidEntryPoint
import main.movies.MoviesScreen
import main.movies.MoviesViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val moviesViewModel: MoviesViewModel = hiltViewModel()
                    MoviesScreen(
                        genres = moviesViewModel.genres,
                        movies = moviesViewModel.movies.value.collectAsLazyPagingItems(),
                    )
                }
            }
        }
    }
}