package com.company.books2trees.domain.model

interface BookModel {
    val id: String
    val name: String
    val cover: String?
    val url: String?
    val extras: String?
}
