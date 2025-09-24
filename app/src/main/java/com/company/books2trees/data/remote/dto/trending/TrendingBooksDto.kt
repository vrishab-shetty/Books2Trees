package com.company.books2trees.data.remote.dto.trending

import com.google.gson.annotations.SerializedName

data class TrendingBooksDto(
    @SerializedName("query")
    val query: String,

    @SerializedName("works")
    val works: List<TrendingBookDto>
)