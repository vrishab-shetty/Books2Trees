package com.company.books2trees.ui.home.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.company.books2trees.ui.models.BookConverter
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.models.SimpleBookModel

@Entity("recent")
class RecentItem : BookConverter {

    @PrimaryKey
    var id: String = ""
    var title: String? = null
    var imgUrl: String? = null
    var url: String? = null
    var extras: String? = null
    var lastAccessed: Long = System.currentTimeMillis()

    override fun toBookModel(): BookModel {
        return SimpleBookModel(
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

    /*** Commented for future use

    companion object {
        fun fromBookModel(model: BookModel): RecentItem {
            return RecentItem().apply {
                this.id = model.id
                this.title = model.name
                this.imgUrl = model.cover
                this.url = model.url
                this.extras = model.extras
            }
        }
    }

     ***/
}