package com.company.books2trees.data.remote.dto.trending

import com.google.gson.annotations.SerializedName

data class TrendingBookDto(
    @SerializedName("key")
    val key: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("author_name")
    val authorNames: List<String>,

    @SerializedName("cover_i")
    val coverId: Int?,

    @SerializedName("first_publish_year")
    val firstPublishYear: Int?,

    @SerializedName("edition_count")
    val editionCount: Int,

    @SerializedName("ebook_access")
    val ebookAccess: String
)