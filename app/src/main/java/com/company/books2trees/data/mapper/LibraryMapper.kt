package com.company.books2trees.data.mapper

import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.profile.LibraryPageItem


fun BookModel.toLibraryItemEntity(categoryId: LibraryPageItem.CategoryId): LibraryBookEntity {
    val entity = LibraryBookEntity(
        id = this.id,
        title = this.name,
        imgUrl = this.cover,
        url = this.url,
        categoryId = categoryId
    )

    return entity
}

fun LibraryBookEntity.toBookModelFromLibrary(): BookModel {
    return BookModel(
        id = this.id,
        name = this.title,
        cover = this.imgUrl,
        url = this.url,
        subtext = null
    )
}