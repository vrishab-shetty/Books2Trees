package com.company.books2trees.data.repository

import com.company.books2trees.data.local.datasource.api.PdfLocalDataSource
import com.company.books2trees.data.mapper.toPdfItem
import com.company.books2trees.data.mapper.toPdfModel
import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.domain.repository.PdfRepository
import kotlinx.coroutines.flow.map

class PdfRepositoryImpl(
    private val localDataSource: PdfLocalDataSource
) : PdfRepository {

    override suspend fun getItemsFlow() =
        localDataSource.getItems().map { list -> list.map { it.toPdfModel() } }

    override suspend fun addPdf(model: PdfModel) {
        localDataSource.insertPdf(model.toPdfItem())
    }

    override suspend fun removePdf(modelId: String) {
        localDataSource.deletePdf(modelId)
    }

    override suspend fun getItemById(modelId: String) =
        localDataSource.findItemById(modelId)?.toPdfModel()
}