package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.domain.repository.PdfRepository

class DeletePdfUseCase(private val pdfRepository: PdfRepository) {
    suspend operator fun invoke(model: PdfModel) {
        pdfRepository.removePdf(model.id)
    }
}