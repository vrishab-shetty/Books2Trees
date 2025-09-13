package com.company.books2trees.data.remote.dto

import com.google.gson.annotations.SerializedName


data class SearchResultDto(
    @SerializedName("numFound")
    val numFound: Int,

    @SerializedName("start")
    val start: Int,

    @SerializedName("q")
    val query: String,

    @SerializedName("docs")
    val searchResults: List<SearchBookDto>
)
