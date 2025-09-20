package com.company.books2trees.data.local.pdf.mapper

import androidx.core.net.toUri
import com.company.books2trees.data.local.pdf.model.PdfItem
import com.company.books2trees.domain.model.PdfModel

fun toPdfModel(entity: PdfItem): PdfModel {
    return PdfModel(
        id = entity.id,
        name = entity.name,
        info = entity.info,
        uri = entity.uriString.toUri(),
        thumbnail = entity.thumbnailPath
    )
}