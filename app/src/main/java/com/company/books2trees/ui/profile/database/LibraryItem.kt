package com.company.books2trees.ui.profile.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.company.books2trees.ui.models.BookConverter
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.models.SimpleBookModel
import com.company.books2trees.ui.profile.LibraryPageItem.CategoryId

@Entity(tableName = "library")
data class LibraryItem(
    @PrimaryKey
    var id: String,
    var title: String,
    var imgUrl: String? = null,
    var url: String? = null,
    var categoryId: CategoryId = CategoryId.None
) : BookConverter {


    override fun toBookModel(): BookModel {
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
