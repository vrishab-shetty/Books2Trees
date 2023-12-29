package com.company.books2trees.ui.models

import androidx.room.TypeConverter
import com.company.books2trees.ui.profile.LibraryPageItem.CategoryId

class Converters {

    @TypeConverter
    fun toCategoryId(value: String) = CategoryId.valueOf(value)

    @TypeConverter
    fun fromCategoryId(value: CategoryId) = value.name
}