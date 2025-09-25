package com.company.books2trees.data.local.datasource.api

import com.company.books2trees.data.local.model.PdfItem
import kotlinx.coroutines.flow.Flow

interface PdfLocalDataSource {
    /**
     * Gets a reactive stream of all locally stored PDF items.
     */
    fun getItems(): Flow<List<PdfItem>>

    /**
     * Inserts a new PDF item into the local data source.
     */
    suspend fun insertPdf(entity: PdfItem)

    /**
     * Deletes a PDF item from the local data source by its ID.
     */
    suspend fun deletePdf(modelId: String)

    /**
     * Finds a specific PDF item by its ID.
     * @return The PdfItem if found, otherwise null.
     */
    suspend fun findItemById(id: String): PdfItem?
}