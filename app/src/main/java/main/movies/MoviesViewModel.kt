package main.movies

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesRepo: MoviesRepo) : ViewModel() {

    val genres = mutableListOf<List<Any>>()
    val movies = mutableStateOf(moviesRepo.getMovies().cachedIn(viewModelScope))

    init {
        getGenres()
    }

    fun getMovies(genre: String? = null) {
        viewModelScope.launch {
            movies.value = moviesRepo.getMovies(genre = genre)
        }
    }

    private fun getGenres() {
        viewModelScope.launch {
            moviesRepo.getGenres()?.forEach { genre ->
                genres.add(genre)
            }
        }
    }
}