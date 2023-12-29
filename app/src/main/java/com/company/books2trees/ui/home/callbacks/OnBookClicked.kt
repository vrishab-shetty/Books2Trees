package com.company.books2trees.ui.home.callbacks

import com.company.books2trees.ui.models.BookModel

interface OnBookClicked {
    fun openBook(model: BookModel)
}