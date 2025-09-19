package com.company.books2trees.domain.model

import android.net.Uri
import java.util.UUID

data class PdfModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    var thumbnail: String? = null,
    val info: String? = null,
    val uri: Uri
)