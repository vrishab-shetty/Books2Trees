package com.company.books2trees.domain.repository

import com.company.books2trees.domain.model.PdfModel
import kotlinx.coroutines.flow.Flow

interface PdfRepository {

    suspend fun getItemsFlow(): Flow<List<PdfModel>>

    suspend fun addPdf(model: PdfModel)

    suspend fun removePdf(modelId: String)

    suspend fun getItemById(modelId: String): PdfModel?
}