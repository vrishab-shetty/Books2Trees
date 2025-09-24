package com.company.books2trees.domain.use_case

import android.net.Uri
import com.company.books2trees.domain.repository.PdfRepository

class AddPdfUseCase(private val pdfRepository: PdfRepository) {
    suspend operator fun invoke(uri: Uri) {
        pdfRepository.addPdf(uri)
    }
}
