package com.company.books2trees.data.local

import androidx.room.TypeConverter
import com.company.books2trees.presentation.profile.LibraryPageItem

class Converters {

    @TypeConverter
    fun toCategoryId(value: String) = LibraryPageItem.CategoryId.valueOf(value)

    @TypeConverter
    fun fromCategoryId(value: LibraryPageItem.CategoryId) = value.name
}