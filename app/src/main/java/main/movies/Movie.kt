package main.movies

data class Movie(
    val genres: List<String?>? = null,
    val id: String? = null,
    val overview: String? = null,
    val release_date: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val url: String? = null
)