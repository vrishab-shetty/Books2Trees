package com.company.books2trees.domain.repository

import android.net.Uri
import com.company.books2trees.domain.model.PdfModel
import kotlinx.coroutines.flow.Flow

interface PdfRepository {

    suspend fun getItemsFlow(): Flow<List<PdfModel>>

    suspend fun addPdf(uri: Uri)

    suspend fun removePdf(modelId: String)
}