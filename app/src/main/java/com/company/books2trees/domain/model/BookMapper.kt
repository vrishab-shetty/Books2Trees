package com.company.books2trees.domain.model

import com.company.books2trees.data.model.LibraryItem
import com.company.books2trees.data.model.RecentItem
import com.company.books2trees.data.remote.dto.BookDetailDto
import com.company.books2trees.data.remote.dto.SearchBookDto
import com.company.books2trees.data.remote.dto.SubjectBookDto
import com.company.books2trees.data.remote.dto.TrendingBookDto

object BookMapper {

    private const val OPEN_LIBRARY_URL = "https://openlibrary.org"
    private const val COVERS_URL = "https://covers.openlibrary.org"

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

    fun LibraryItem.toBookModel(): BookModel {
        return BookModel(
            id = this.id,
            name = this.title,
            cover = this.imgUrl,
            url = this.url,
            subtext = null
        )
    }

    fun RecentItem.toBookModel(): BookModel {
        return BookModel(
            id = this.id,
            name = this.title ?: "",
            cover = this.imgUrl,
            url = this.url,
            subtext = this.extras
        )
    }


}