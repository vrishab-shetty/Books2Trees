package com.company.books2trees.data.remote.dto

import com.google.gson.annotations.SerializedName


data class BookDetailDto(
    @SerializedName("key") val key: String,
    @SerializedName("title") val title: String,
    @SerializedName("covers") val coverIds: List<Int>? = null,
    // OpenLibrary sometimes returns description as a string, sometimes an object
    // A custom serializer would be needed for the object case, but for simplicity,
    // we'll assume it's often missing or you'll fetch it separately.
    @SerializedName("description") val description: String? = "No description available."
)