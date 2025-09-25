package com.company.books2trees.data.local.datasource.api

import com.company.books2trees.data.local.datasource.impl.PdfLocalDataSource
import com.company.books2trees.data.local.model.PdfItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class PdfLocalDataSourceImpl : PdfLocalDataSource {
    private val pdfsFlow = MutableStateFlow<Map<String, PdfItem>>(emptyMap())

    override fun getItems(): Flow<List<PdfItem>> = pdfsFlow.map { it.values.toList() }

    override suspend fun insertPdf(entity: PdfItem) = withContext(Dispatchers.IO) {
        pdfsFlow.update { currentMap ->
            currentMap + (entity.id to entity)
        }
    }

    override suspend fun deletePdf(modelId: String) = withContext(Dispatchers.IO) {
        pdfsFlow.update { currentMap ->
            currentMap - modelId
        }
    }

    override suspend fun findItemById(id: String): PdfItem? = withContext(Dispatchers.IO) {
        pdfsFlow.value[id]
    }
}