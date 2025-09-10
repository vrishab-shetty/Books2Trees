package com.company.books2trees.data.remote.dto

import com.google.gson.annotations.SerializedName


data class AuthorDto(
    @SerializedName("key")
    val key: String,

    @SerializedName("name")
    val name: String
)