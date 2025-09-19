package com.company.books2trees.data.repository

import android.net.Uri
import com.company.books2trees.data.local.PdfLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PdfRepository(private val pdfLocalDataSource: PdfLocalDataSource) {

    fun getItems() = pdfLocalDataSource.getItems()

    suspend fun addPdf(uri: Uri) = withContext(Dispatchers.IO) {
        pdfLocalDataSource.addPdf(uri)
    }

    suspend fun removePdf(modelId: String) = withContext(Dispatchers.IO) {
        pdfLocalDataSource.removePdf(modelId)
    }

    fun findItemById(id: String) = pdfLocalDataSource.findItemById(id)
}
