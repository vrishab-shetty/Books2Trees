package com.company.books2trees.presentation.info

import com.company.books2trees.domain.model.PdfModel

sealed class PdfModelViewState {
    object Loading : PdfModelViewState()
    data class Content(val model: PdfModel) : PdfModelViewState()
    data class Error(val throwable: Throwable) : PdfModelViewState()
}