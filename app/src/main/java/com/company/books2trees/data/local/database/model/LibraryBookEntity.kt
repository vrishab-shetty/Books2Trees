package com.company.books2trees.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library")
data class LibraryBookEntity(
    @PrimaryKey
    var id: String,
    var title: String,
    var imgUrl: String? = null,
    var url: String? = null,
    var category: String
)