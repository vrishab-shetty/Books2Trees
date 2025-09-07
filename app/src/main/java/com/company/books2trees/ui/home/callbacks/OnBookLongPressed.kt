package com.company.books2trees.ui.home.callbacks

import android.view.View
import com.company.books2trees.ui.models.BookModel

interface OnBookLongPressed {
    fun showOptionsMenu(model: BookModel, itemView: View)
}