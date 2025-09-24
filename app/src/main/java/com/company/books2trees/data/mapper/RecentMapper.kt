package com.company.books2trees.data.mapper

import com.company.books2trees.data.local.database.model.RecentBookEntity
import com.company.books2trees.domain.model.BookModel

fun BookModel.toRecentItemEntity(): RecentBookEntity {
    val entity = RecentBookEntity()
    entity.id = this.id
    entity.extras = this.subtext
    entity.imgUrl = this.cover
    entity.title = this.name
    entity.url = this.url

    return entity
}

fun RecentBookEntity.toBookModelFromRecent(): BookModel {
    return BookModel(
        id = this.id,
        name = this.title ?: "",
        cover = this.imgUrl,
        url = this.url,
        subtext = this.extras
    )
}