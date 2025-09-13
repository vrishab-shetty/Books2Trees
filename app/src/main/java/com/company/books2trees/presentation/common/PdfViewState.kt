package com.company.books2trees.presentation.common

import android.graphics.pdf.PdfRenderer

sealed class PdfViewState {
    object Loading : PdfViewState()
    data class Content(val renderer: PdfRenderer) : PdfViewState()
    data class Error(val throwable: Throwable) : PdfViewState()
}
