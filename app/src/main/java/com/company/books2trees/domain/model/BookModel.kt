package com.company.books2trees.domain.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookModel(
    val id: String,
    val name: String,
    val cover: String?,
    val url: String?,
    val subtext: String?
) : Parcelable
