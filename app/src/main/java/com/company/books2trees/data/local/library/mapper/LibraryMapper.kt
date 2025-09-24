package com.company.books2trees.data.local.library.mapper

import com.company.books2trees.data.local.library.model.LibraryItem
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.profile.LibraryPageItem


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