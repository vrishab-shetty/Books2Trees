package com.company.books2trees.data.local.pdf.impl

import android.net.Uri
import androidx.core.net.toUri
import com.company.books2trees.data.local.core.FileUtil
import com.company.books2trees.data.local.pdf.PdfLocalDataSource
import com.company.books2trees.data.local.pdf.PdfPageProviderFactory
import com.company.books2trees.data.local.pdf.mapper.toPdfModel
import com.company.books2trees.data.local.pdf.model.PdfItem
import com.company.books2trees.domain.repository.PdfRepository
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.UUID

class PdfRepositoryImpl(
    private val fileUtil: FileUtil,
    private val pdfLocalDataSource: PdfLocalDataSource,
    private val pageProviderFactory: PdfPageProviderFactory
) : PdfRepository {

    private val DIRECTORY_THUMBNAILS = "thumbnails"

    override suspend fun getItemsFlow() =
        pdfLocalDataSource.getItems().map { it.map(::toPdfModel) }

    @Throws(IOException::class)
    override suspend fun addPdf(uri: Uri) {
        val name = fileUtil.getFileName(uri) ?: throw IOException("Could not resolve file name")
        val info = fileUtil.getFileSize(uri)

        val newId = UUID.randomUUID().toString()

        val tempPageProvider = pageProviderFactory.create(uri)

        val thumbnailBitmap = tempPageProvider.use { provider ->
            provider.getPage(0, 150)
        }

        val thumbnailPath = thumbnailBitmap?.let {
            fileUtil.saveBitmapFileIntoExternalStorageWithTitle(
                it,
                newId,
                DIRECTORY_THUMBNAILS
            ).toString()
        }

        val entity = PdfItem(
            id = newId,
            name = name,
            info = info,
            uriString = uri.toString(),
            thumbnailPath = thumbnailPath
        )

        pdfLocalDataSource.savePdf(entity)
    }

    override suspend fun removePdf(modelId: String) {
        val entity = pdfLocalDataSource.findItemById(modelId)
        entity?.thumbnailPath?.let {
            fileUtil.deleteFile(it.toUri())
        }
        pdfLocalDataSource.removePdf(modelId)
    }
}