package com.company.books2trees.domain.model

data class LibraryBookModel(
    val id: String,
    val name: String,
    val cover: String?,
    val url: String?,
    var categoryId: CategoryId
)
