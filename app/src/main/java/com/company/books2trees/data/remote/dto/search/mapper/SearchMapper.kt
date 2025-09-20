package com.company.books2trees.data.remote.dto.search.mapper

import com.company.books2trees.data.remote.dto.common.mapper.COVERS_URL
import com.company.books2trees.data.remote.dto.common.mapper.OPEN_LIBRARY_URL
import com.company.books2trees.data.remote.dto.search.SearchBookDto
import com.company.books2trees.domain.model.BookModel

fun SearchBookDto.toBookModel(): BookModel {
    val coverUrl = coverId?.let {
        "$COVERS_URL/b/id/$it-L.jpg"
    }

    val subtext = this.authorName?.joinToString(", ")

    return BookModel(
        id = this.key,
        name = this.title,
        cover = coverUrl,
        url = OPEN_LIBRARY_URL + this.key,
        subtext = subtext
    )
}