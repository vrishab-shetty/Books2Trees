package com.company.books2trees.data.repository

import android.net.Uri
import androidx.core.net.toUri
import com.company.books2trees.data.local.datasource.api.PdfLocalDataSource
import com.company.books2trees.data.local.files.FileUtil
import com.company.books2trees.data.local.files.pdf.PdfPageProviderFactory
import com.company.books2trees.data.local.model.PdfItem
import com.company.books2trees.data.mapper.toPdfModel
import com.company.books2trees.domain.repository.PdfRepository
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.UUID

class PdfRepositoryImpl(
    private val fileUtil: FileUtil,
    private val pdfLocalDataSource: PdfLocalDataSource,
    private val pageProviderFactory: PdfPageProviderFactory
) : PdfRepository {

    private val thumbnailDirectory = "thumbnails"

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
                thumbnailDirectory
            ).toString()
        }

        val entity = PdfItem(
            id = newId,
            name = name,
            info = info,
            uriString = uri.toString(),
            thumbnailPath = thumbnailPath
        )

        pdfLocalDataSource.insertPdf(entity)
    }

    override suspend fun removePdf(modelId: String) {
        val entity = pdfLocalDataSource.findItemById(modelId)
        entity?.thumbnailPath?.let {
            fileUtil.deleteFile(it.toUri())
        }
        pdfLocalDataSource.deletePdf(modelId)
    }
}