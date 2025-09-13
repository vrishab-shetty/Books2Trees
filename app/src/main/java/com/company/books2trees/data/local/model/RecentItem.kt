package com.company.books2trees.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("recent")
class RecentItem(
    @PrimaryKey
    var id: String = "",
    var title: String? = null,
    var imgUrl: String? = null,
    var url: String? = null,
    var extras: String? = null,
    var lastAccessed: Long = System.currentTimeMillis()
)