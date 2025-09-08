package com.company.books2trees.presentation.home.callbacks

import com.company.books2trees.domain.model.BookModel

interface OnBookClicked {
    fun openBook(model: BookModel)
}