package main.movies

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
        AllOption(expanded, genres, selectedGenre, fetchMovies)
        for (genre in genres) {
            DropdownMenuItem(
                onClick = {
                    expanded.value = !expanded.value
                    selectedGenre.value = genre.firstOrNull().toString()
                    fetchMovies.invoke(selectedGenre.value)
                }, content = {
                    val genreName = genre.firstOrNull().toString()
                    val genreCount =
                        genre.getOrNull(INDEX_OF_GENRE_COUNT).toString().toDoubleOrNull()?.toInt()
                    Text(text = "$genreName (${genreCount})")
                })
        }
    }
}

@Composable
private fun AllOption(
    expanded: MutableState<Boolean>,
    genres: MutableList<List<Any>>,
    selectedGenre: MutableState<String>,
    fetchMovies: (filter: String?) -> Unit
) {
    val allText = stringResource(id = com.main.movies.R.string.all)
    val totalCount = 0
    DropdownMenuItem(
        onClick = {
            expanded.value = !expanded.value
            selectedGenre.value = allText
            fetchMovies.invoke(null)
        }, content = {
            Text(text = "$allText ($totalCount)")
        })
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
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { launchWebIntent(movie, context) }
    ) {
        Column(modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)) {
            TitleAndYearHeader(movie)
            LineSeparator()
            Overview(movie)
            Genres(movie)
        }
    }
}

@Composable
private fun TitleAndYearHeader(movie: Movie?) {
    Row {
        val indexOfDash = movie?.release_date?.indexOf("-") ?: movie?.release_date?.length
        val releaseYear = if (indexOfDash != null) {
            movie?.release_date?.substring(0, indexOfDash)
        } else {
            movie?.release_date
        }
        Text(text = "${movie?.title ?: ""} $releaseYear")
    }
}

@Composable
private fun LineSeparator() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .padding(horizontal = 2.dp)
    )
}

@Composable
private fun Overview(movie: Movie?) {
    Text(text = movie?.overview ?: "")
}

@Composable
private fun Genres(movie: Movie?) {
    movie?.genres?.let { genres ->
        val genreStringBuilder = StringBuilder()
        genres.forEachIndexed { index, genre ->
            genreStringBuilder.append(genre)
            if (shouldAddSeparator(index, genres)) {
                genreStringBuilder.append(", ")
            }
        }
        Text(text = genreStringBuilder.toString())
    }
}

private fun launchWebIntent(movie: Movie?, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie?.url))
    ContextCompat.startActivity(
        context,
        Intent.createChooser(intent, "View more details..."),
        null
    )
}

private fun shouldAddSeparator(
    index: Int,
    genres: List<String?>
): Boolean = genres.size > 1 && index < genres.size - 1
