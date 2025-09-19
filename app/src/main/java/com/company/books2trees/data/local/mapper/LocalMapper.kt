package com.company.books2trees.data.local.mapper

import androidx.core.net.toUri
import com.company.books2trees.data.local.model.LibraryItem
import com.company.books2trees.data.local.model.PdfItem
import com.company.books2trees.data.local.model.RecentItem
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.presentation.profile.LibraryPageItem

fun BookModel.toRecentItemEntity(): RecentItem {
    val entity = RecentItem()
    entity.id = this.id
    entity.extras = this.subtext
    entity.imgUrl = this.cover
    entity.title = this.name
    entity.url = this.url

    return entity
}

fun BookModel.toLibraryItemEntity(categoryId: LibraryPageItem.CategoryId): LibraryItem {
    val entity = LibraryItem(
        id = this.id,
        title = this.name,
        imgUrl = this.cover,
        url = this.url,
        categoryId = categoryId
    )

    return entity
}

fun LibraryItem.toBookModel(): BookModel {
    return BookModel(
        id = this.id,
        name = this.title,
        cover = this.imgUrl,
        url = this.url,
        subtext = null
    )
}


fun RecentItem.toBookModel(): BookModel {
    return BookModel(
        id = this.id,
        name = this.title ?: "",
        cover = this.imgUrl,
        url = this.url,
        subtext = this.extras
    )
}

fun toPdfModel(entity: PdfItem): PdfModel {
    return PdfModel(
        id = entity.id,
        name = entity.name,
        info = entity.info,
        uri = entity.uriString.toUri(),
        thumbnail = entity.thumbnailPath
    )
}