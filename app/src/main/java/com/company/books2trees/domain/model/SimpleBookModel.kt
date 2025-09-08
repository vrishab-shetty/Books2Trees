package com.company.books2trees.domain.model

class SimpleBookModel(
    override val id: String,
    override val name: String,
    override val cover: String?,
    override val url: String?,
    override val extras: String?
) : BookModel {
}