package main.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import app.AppConstants.INDEX_OF_GENRE_COUNT

@Composable
fun MoviesScreen(
    genres: MutableList<List<Any>>,
    movies: LazyPagingItems<Movie>,
    fetchMovies: (filter: String?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val expanded = remember { mutableStateOf(false) }
        val selectGenre = stringResource(id = com.main.movies.R.string.select_a_genre)
        val selectedGenre = remember { mutableStateOf(selectGenre) }
        GenresDropDown(expanded = expanded, selectedGenre = selectedGenre)
        GenresList(
            expanded = expanded,
            genres = genres,
            selectedGenre = selectedGenre,
            fetchMovies = fetchMovies
        )
        Movies(movies)
    }
}

@Composable
private fun GenresDropDown(
    expanded: MutableState<Boolean>,
    selectedGenre: MutableState<String>
) {
    IconButton(onClick = { expanded.value = !expanded.value }) {
        Row {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Open Genres"
            )
            Text(text = selectedGenre.value)
        }
    }
}

@Composable
private fun GenresList(
    expanded: MutableState<Boolean>,
    genres: MutableList<List<Any>>,
    selectedGenre: MutableState<String>,
    fetchMovies: (filter: String?) -> Unit
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = !expanded.value }) {
        for (genre in genres) {
            DropdownMenuItem(
                onClick = {
                    expanded.value = !expanded.value
                    selectedGenre.value = genre.firstOrNull().toString()
                    fetchMovies.invoke(selectedGenre.value)
                }, content = {
                    val genreName = genre.firstOrNull().toString()
                    val genreCount = genre.getOrNull(INDEX_OF_GENRE_COUNT)
                    Text(text = "$genreName (${genreCount})")
                })
        }
    }
}

@Composable
private fun Movies(movies: LazyPagingItems<Movie>) {
    when (movies.loadState.refresh) {
        LoadState.Loading -> {

        }
        is LoadState.Error -> {

        }
        else -> {
            LazyColumn {
                items(
                    count = movies.itemCount,
                    key = movies.itemKey(),
                    contentType = movies.itemContentType(),
                ) { index ->
                    movies[index]?.let { movie ->
                        MovieCard(movie = movie)
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieCard(movie: Movie?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(text = movie?.title ?: "None")
    }
}
