package com.company.books2trees.domain.model

data class BookModel(
    val id: String,
    val name: String,
    val cover: String?,
    val url: String?,
    val subtext: String?
)
