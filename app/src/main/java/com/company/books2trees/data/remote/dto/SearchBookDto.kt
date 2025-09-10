package com.company.books2trees.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchBookDto(
    @SerializedName("key")
    val key: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("author_name")
    val authorName: List<String>?,

    @SerializedName("cover_i")
    val coverId: Int?,

    @SerializedName("first_publish_year")
    val firstPublishYear: Int?
)
