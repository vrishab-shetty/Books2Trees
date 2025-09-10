package com.company.books2trees.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SubjectBookDto(
    @SerializedName("key")
    val key: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("authors")
    val authors: List<AuthorDto>?,

    @SerializedName("cover_id")
    val coverId: Int?,

    @SerializedName("first_publish_year")
    val firstPublishYear: Int,

    @SerializedName("edition_count")
    val editionCount: Int
)

