package com.company.books2trees.data.remote.dto.common.mapper

import com.company.books2trees.data.remote.dto.common.BookDetailDto
import com.company.books2trees.data.remote.dto.common.SubjectBookDto
import com.company.books2trees.domain.model.BookModel

const val OPEN_LIBRARY_URL = "https://openlibrary.org"
const val COVERS_URL = "https://covers.openlibrary.org"

fun BookDetailDto.toBookModel(): BookModel {
    val coverUrl = this.coverIds?.firstOrNull()?.let { "$COVERS_URL/b/id/$it-L.jpg" }

    return BookModel(
        id = this.key,
        name = this.title,
        cover = coverUrl,
        url = OPEN_LIBRARY_URL + this.key,
        subtext = this.description
    )
}

fun SubjectBookDto.toBookModel(): BookModel {
    val coverUrl = coverId?.let {
        "$COVERS_URL/b/id/$it-L.jpg"
    }

    val subtext = this.authors?.joinToString(", ") { it.name }

    return BookModel(
        id = this.key,
        name = this.title,
        cover = coverUrl,
        url = OPEN_LIBRARY_URL + this.key,
        subtext = subtext
    )
}