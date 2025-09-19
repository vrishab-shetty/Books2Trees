package com.company.books2trees.data.repository

import android.net.Uri
import com.company.books2trees.data.local.PdfLocalDataSource
import com.company.books2trees.domain.repository.PdfRepository

class PdfRepositoryImpl(private val pdfLocalDataSource: PdfLocalDataSource) : PdfRepository {

    override suspend fun getItemsFlow() = pdfLocalDataSource.getItems()

    override suspend fun addPdf(uri: Uri) {
        pdfLocalDataSource.addPdf(uri)
    }

    override suspend fun removePdf(modelId: String) {
        pdfLocalDataSource.removePdf(modelId)
    }
}
