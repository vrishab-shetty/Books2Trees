package com.company.books2trees.domain.use_case

import androidx.core.net.toUri
import com.company.books2trees.data.local.files.FileUtil
import com.company.books2trees.domain.repository.PdfRepository

class RemovePdfUseCase(private val pdfRepository: PdfRepository, private val fileUtil: FileUtil) {
    suspend operator fun invoke(id: String) {
        val pdfToDelete = pdfRepository.getItemById(id)
        pdfToDelete?.thumbnailPath?.let { path ->
            fileUtil.deleteFile(path.toUri())
        }
        pdfRepository.removePdf(id)
    }
}