package com.company.books2trees.data.local.pdf

import android.content.Context
import android.net.Uri

class PdfPageProviderFactory(private val context: Context) {
    fun create(uri: Uri): PdfPageProvider {
        return PdfPageProvider(context, uri)
    }
}