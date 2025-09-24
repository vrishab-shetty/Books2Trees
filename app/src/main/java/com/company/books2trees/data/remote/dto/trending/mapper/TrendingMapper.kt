package com.company.books2trees.data.remote.dto.trending.mapper

import com.company.books2trees.data.remote.dto.common.mapper.COVERS_URL
import com.company.books2trees.data.remote.dto.common.mapper.OPEN_LIBRARY_URL
import com.company.books2trees.data.remote.dto.trending.TrendingBookDto
import com.company.books2trees.domain.model.BookModel

fun TrendingBookDto.toBookModel(): BookModel {
    val coverUrl = coverId?.let { "$COVERS_URL/b/id/$it-L.jpg" }

    val subtext = this.authorNames.joinToString(", ")

    return BookModel(
        id = this.key,
        name = this.title,
        cover = coverUrl,
        url = OPEN_LIBRARY_URL + this.key,
        subtext = subtext
    )
}