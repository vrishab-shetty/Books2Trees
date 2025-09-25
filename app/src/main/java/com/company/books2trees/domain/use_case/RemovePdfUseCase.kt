package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.PdfRepository

class RemovePdfUseCase(private val pdfRepository: PdfRepository) {
    suspend operator fun invoke(id: String) {
        pdfRepository.removePdf(id)
    }
}