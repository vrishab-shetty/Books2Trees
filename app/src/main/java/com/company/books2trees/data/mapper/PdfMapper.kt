package com.company.books2trees.data.mapper

import androidx.core.net.toUri
import com.company.books2trees.data.local.model.PdfItem
import com.company.books2trees.domain.model.PdfModel

fun PdfItem.toPdfModel(): PdfModel {
    return PdfModel(
        id = this.id,
        name = this.name,
        info = this.info,
        uri = this.uriString.toUri(),
        thumbnailPath = this.thumbnailPath
    )
}

fun PdfModel.toPdfItem(): PdfItem {
    return PdfItem(
        id = this.id,
        name = this.name,
        info = this.info,
        uriString = this.uri.toString(),
        thumbnailPath = this.thumbnailPath
    )
}