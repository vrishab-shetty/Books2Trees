package com.company.books2trees.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.company.books2trees.presentation.common.PdfViewState
import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.presentation.info.PdfModelViewState
import com.company.books2trees.data.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object PdfRepository {


    private val pdfList = MutableLiveData<Map<String, PdfModel>>(emptyMap())
    fun getItems(): LiveData<List<PdfModel>> = pdfList.map {
        it.values.toList()
    }

    suspend fun addPdf(context: Context, uri: Uri) = withContext(Dispatchers.IO) {
        try {
            val name = FileUtils.getFileName(context, uri)
            val info = FileUtils.getFileSize(context, uri)

            val model = PdfModel(
                name = name!!, info = info, uri = uri
            )

            val bitmap = renderSinglePage(context, model.uri, 70, 0)
            model.thumbnail = bitmap?.let {
                FileUtils.saveBitmapFileIntoExternalStorageWithTitle(
                    context,
                    it,
                    model.id,
                    FileUtils.DIRECTORY_THUMBNAILS
                )
            }.toString()

            val list = mutableMapOf(model.id to model)
            pdfList.value?.forEach { (k, v) -> list.putIfAbsent(k, v) }
            pdfList.postValue(list)

            PdfModelViewState.Content(model)

        } catch (ioe: IOException) {
            Log.e("PdfRepository", "Couldn't get the file details", ioe)
            PdfModelViewState.Error(ioe)
        }
    }

    suspend fun removePdf(context: Context, modelId: String) =
        withContext(Dispatchers.IO) {
            val model = pdfList.value?.get(modelId)

            model?.let {
                val list = mutableMapOf<String, PdfModel>()
                pdfList.value?.forEach { (k, v) ->
                    list.putIfAbsent(k, v)
                }

                list.remove(modelId)

                pdfList.postValue(list)

                FileUtils.deleteFileWithUri(context, Uri.parse(it.thumbnail))
            }
        }


    fun findItemById(id: String) = pdfList.value?.get(id)

    suspend fun getRenderer(context: Context, model: PdfModel) = withContext(Dispatchers.IO) {
        try {
            val renderer = PdfRenderer(
                (context).contentResolver.openFileDescriptor(
                    model.uri,
                    "r",
                    null
                )!!
            )
            PdfViewState.Content(renderer)
        } catch (ioe: IOException) {
            PdfViewState.Error(ioe)
        }

    }

    private suspend fun renderSinglePage(
        context: Context,
        fileUri: Uri,
        width: Int,
        index: Int
    ): Bitmap? =
        if (index < 0) null
        else withContext(Dispatchers.IO) {
            PdfRenderer(
                context.contentResolver.openFileDescriptor(
                    fileUri, "r", null
                )!!
            ).use { renderer ->
                renderer.openPage(index).renderAndClose(width)
            }
        }


    fun PdfRenderer.Page.renderAndClose(width: Int) = use {
        val bitmap = FileUtils.getScaledBitmap(it.width, it.height, width)
        render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        bitmap
    }
}
