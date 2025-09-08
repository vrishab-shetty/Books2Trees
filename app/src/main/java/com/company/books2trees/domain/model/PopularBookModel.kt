package com.company.books2trees.domain.model

data class PopularBookModel(
    override val id: String,
    val position: Int,
    override val name: String,
    val rating: Double,
    override val cover: String,
    override val url: String
) : BookModel {

    // The 'extras' property is now a computed property, satisfying the interface.
    override val extras: String
        get() = "$rating \u2605"
}