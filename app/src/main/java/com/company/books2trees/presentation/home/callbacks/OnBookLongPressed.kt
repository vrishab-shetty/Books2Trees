package com.company.books2trees.presentation.home.callbacks

import android.view.View
import com.company.books2trees.domain.model.BookModel

interface OnBookLongPressed {
    fun showOptionsMenu(model: BookModel, itemView: View)
}