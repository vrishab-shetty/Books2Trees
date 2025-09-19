package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.domain.repository.PdfRepository
import kotlinx.coroutines.flow.Flow

class GetPdfsUseCase(private val pdfRepository: PdfRepository) {
    suspend operator fun invoke(): Flow<List<PdfModel>> {
        return pdfRepository.getItemsFlow()
    }
}