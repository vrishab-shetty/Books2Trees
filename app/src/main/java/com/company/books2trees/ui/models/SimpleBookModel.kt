package com.company.books2trees.ui.models

class SimpleBookModel(
    override val id: String,
    override val name: String,
    override val cover: String?,
    override val url: String?,
    override val extras: String?
) : BookModel {
}