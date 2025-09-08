package com.company.books2trees.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.model.SimpleBookModel
import com.company.books2trees.presentation.profile.LibraryPageItem

@Entity(tableName = "library")
data class LibraryItem(
    @PrimaryKey
    var id: String,
    var title: String,
    var imgUrl: String? = null,
    var url: String? = null,
    var categoryId: LibraryPageItem.CategoryId = LibraryPageItem.CategoryId.None
) {


    fun toBookModel(): BookModel {
        return SimpleBookModel(
            id,
            title,
            imgUrl,
            url,
            null
        )
    }

    override fun toString(): String {
        return "LibraryItem(id='$id', title=$title, imgUrl=$imgUrl, url=$url, categoryId=$categoryId)"
    }


}