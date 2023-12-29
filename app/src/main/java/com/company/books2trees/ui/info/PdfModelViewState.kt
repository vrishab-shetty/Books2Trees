package com.company.books2trees.ui.info

import com.company.books2trees.ui.models.PdfModel

sealed class PdfModelViewState {
    object Loading: PdfModelViewState()
    data class Content(val model: PdfModel): PdfModelViewState()
    data class Error(val throwable: Throwable): PdfModelViewState()
}