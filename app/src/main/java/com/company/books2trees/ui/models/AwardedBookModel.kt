package com.company.books2trees.ui.models

data class AwardedBookModel(
    override val id: String,
    val category: String,
    override val name: String,
    override val cover: String,
    override val url: String
) : BookModel {

    // 'extras' maps directly to the category property.
    override val extras: String
        get() = category
}
