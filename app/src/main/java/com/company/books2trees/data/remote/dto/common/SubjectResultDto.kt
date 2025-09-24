package com.company.books2trees.data.remote.dto.common

import com.google.gson.annotations.SerializedName

data class SubjectResultDto(
    @SerializedName("key")
    val key: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("work_count")
    val workCount: Int,

    @SerializedName("works")
    val works: List<SubjectBookDto>
)