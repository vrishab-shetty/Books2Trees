package com.company.books2trees.ui.home.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.company.books2trees.ui.models.BookConverter
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.profile.LibraryPageItem

@Entity("recent")
class RecentItem: BookConverter {

    @PrimaryKey
    var id: String = ""
    var title: String? = null
    var imgUrl: String? = null
    var url: String? = null
    var extras: String? = null

    override fun toBookModel(): BookModel {
        return BookModel(
            id,
            title ?: "",
            imgUrl,
            url,
            extras
        )
    }

    override fun toString(): String {
        return "RecentItem(id='$id', title=$title, imgUrl=$imgUrl, url=$url, extras=$extras)"
    }

    companion object {
        fun fromBookModel(model: BookModel): RecentItem {
            val instance = RecentItem()

            instance.id = model.id
            instance.title = model.name
            instance.imgUrl = model.cover
            instance.url = model.url
            instance.extras = model.extras

            return instance
        }
    }
}