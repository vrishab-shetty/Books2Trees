package com.company.books2trees.domain.use_case

import android.net.Uri
import com.company.books2trees.data.local.files.FileUtil
import com.company.books2trees.data.local.files.pdf.PdfPageProviderFactory
import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.domain.repository.PdfRepository
import java.io.IOException
import java.util.UUID

class AddPdfUseCase(
    private val pdfRepository: PdfRepository,
    private val pageProviderFactory: PdfPageProviderFactory,
    private val fileUtil: FileUtil
) {
    private val thumbnailDirectory = "thumbnails"

    @Throws(IOException::class)
    suspend operator fun invoke(uri: Uri) {
        val name = fileUtil.getFileName(uri) ?: throw IOException("Could not resolve file name")
        val info = fileUtil.getFileSize(uri)
        val newId = UUID.randomUUID().toString()

        val tempPageProvider = pageProviderFactory.create(uri)
        val thumbnailBitmap = tempPageProvider.use { provider ->
            provider.getPage(0, 150)
        }
        val thumbnailPath = thumbnailBitmap?.let {
            fileUtil.saveBitmapFileIntoExternalStorageWithTitle(
                it, newId, thumbnailDirectory
            ).toString()
        }

        val pdfModel = PdfModel(
            id = newId,
            name = name,
            info = info,
            uri = uri,
            thumbnailPath = thumbnailPath
        )

        pdfRepository.addPdf(pdfModel)
    }
}
