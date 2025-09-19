package com.company.books2trees.data.local

import android.net.Uri
import androidx.core.net.toUri
import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.data.utils.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.IOException

// ToDo: Fix Thumbnail issue
class PdfLocalDataSource(private val fileUtil: FileUtil, /*private val pdfPageProvider: PdfPageProvider*/) {
    private val DIRECTORY_THUMBNAILS = "Thumbnails"

    private val pdfsFlow = MutableStateFlow<Map<String, PdfModel>>(emptyMap())
    fun getItems(): Flow<List<PdfModel>> = pdfsFlow.map { it.values.toList() }

    @Throws(IOException::class)
    suspend fun addPdf(uri: Uri) {
        val name = fileUtil.getFileName(uri) ?: throw IOException("Could not resolve file name")
        val info = fileUtil.getFileSize(uri)

        val model = PdfModel(
            name = name, info = info, uri = uri
        )

//        val bitmap = pdfPageProvider.renderPageToBitmap(model.uri, 0, 150)

//        model.thumbnail = bitmap?.let {
//            fileUtil.saveBitmapFileIntoExternalStorageWithTitle(
//                it,
//                model.id,
//                DIRECTORY_THUMBNAILS
//            ).toString()
//        }
        pdfsFlow.update { currentMap ->
            currentMap + (model.id to model)
        }
    }

    suspend fun removePdf(modelId: String) = withContext(Dispatchers.IO) {
        val model = pdfsFlow.value[modelId]

        model?.let {
            val list = mutableMapOf<String, PdfModel>()
            pdfsFlow.value.forEach { (k, v) ->
                list.putIfAbsent(k, v)
            }

            list.remove(modelId)

            pdfsFlow.update { list }

//            fileUtil.deleteFile(it.thumbnail!!.toUri())
        }
    }


    fun findItemById(id: String) = pdfsFlow.value[id]
}