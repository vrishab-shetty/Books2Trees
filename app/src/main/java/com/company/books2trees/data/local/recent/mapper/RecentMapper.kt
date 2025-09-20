package com.company.books2trees.data.local.recent.mapper

import com.company.books2trees.data.local.recent.model.RecentItem
import com.company.books2trees.domain.model.BookModel

fun BookModel.toRecentItemEntity(): RecentItem {
    val entity = RecentItem()
    entity.id = this.id
    entity.extras = this.subtext
    entity.imgUrl = this.cover
    entity.title = this.name
    entity.url = this.url

    return entity
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