package com.company.books2trees.data.local.pdf

import com.company.books2trees.data.local.pdf.model.PdfItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class PdfLocalDataSource {
    private val pdfsFlow = MutableStateFlow<Map<String, PdfItem>>(emptyMap())

    suspend fun getItems(): Flow<List<PdfItem>> = withContext(Dispatchers.IO) {
        pdfsFlow.map { it.values.toList() }
    }

    suspend fun savePdf(entity: PdfItem) = withContext(Dispatchers.IO) {
        pdfsFlow.update { currentMap ->
            currentMap + (entity.id to entity)
        }
    }

    suspend fun removePdf(modelId: String) = withContext(Dispatchers.IO) {
        pdfsFlow.update { currentMap ->
            currentMap - modelId
        }
    }

    suspend fun findItemById(id: String): PdfItem? = withContext(Dispatchers.IO) {
        pdfsFlow.value[id]
    }
}