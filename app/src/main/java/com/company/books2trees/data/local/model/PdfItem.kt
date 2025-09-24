package com.company.books2trees.data.local.model

data class PdfItem(
    val id: String,
    val name: String,
    val info: String?,
    val uriString: String,
    val thumbnailPath: String?
)