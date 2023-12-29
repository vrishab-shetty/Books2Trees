package com.company.books2trees.ui.common

import android.graphics.pdf.PdfRenderer

sealed class PdfViewState {
    object Loading: PdfViewState()
    data class Content(val renderer: PdfRenderer): PdfViewState()
    data class Error(val throwable: Throwable): PdfViewState()
}
