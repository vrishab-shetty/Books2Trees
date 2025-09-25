package com.company.books2trees.data.mapper

import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.model.CategoryId
import com.company.books2trees.domain.model.LibraryBookModel


fun LibraryBookModel.toLibraryItemEntity(): LibraryBookEntity {
    val entity = LibraryBookEntity(
        id = this.id,
        title = this.name,
        imgUrl = this.cover,
        url = this.url,
        category = this.categoryId.name
    )

    return entity
}

fun LibraryBookModel.toBookModelFromLibraryModel(): BookModel {
    return BookModel(
        id = this.id,
        name = this.name,
        cover = this.cover,
        url = this.url,
        subtext = null
    )
}

fun LibraryBookEntity.toLibraryBookModel(): LibraryBookModel {
    return LibraryBookModel(
        id = this.id,
        name = this.title,
        cover = this.imgUrl,
        url = this.url,
        categoryId = CategoryId.valueOf(this.category)
    )
}