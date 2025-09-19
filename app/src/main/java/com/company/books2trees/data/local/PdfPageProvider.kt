package com.company.books2trees.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.util.LruCache
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.IOException

class PdfPageProvider(context: Context, fileUri: Uri) : AutoCloseable {
    // A cache to store rendered bitmaps, improving performance by avoiding re-rendering.
    private val pageBitmapCache = LruCache<Int, Bitmap>(10) // Cache up to 10 pages

    // A Mutex to ensure thread-safe access to the renderer, as PdfRenderer is not thread-safe.
    private val rendererMutex = Mutex()

    private val fileDescriptor = context.contentResolver.openFileDescriptor(fileUri, "r")
    private val pdfRenderer = PdfRenderer(fileDescriptor!!)

    val pageCount: Int = pdfRenderer.pageCount

    /**
     * Renders a specific page to a Bitmap of a given width. This is the primary
     * method the UI will call to get pages. It checks the cache first.
     *
     * @param pageIndex The index of the page to render.
     * @param renderWidth The width to render the bitmap at. Height is scaled proportionally.
     * @return The rendered Bitmap, or null if rendering fails.
     */
    @Throws(IOException::class)
    suspend fun getPage(pageIndex: Int, renderWidth: Int): Bitmap? = withContext(Dispatchers.IO) {
        if (pageIndex < 0 || pageIndex >= pageCount) {
            return@withContext null
        }

        // Return cached bitmap if it exists
        pageBitmapCache.get(pageIndex)?.let { return@withContext it }

        // Use a Mutex to ensure only one thread accesses the non-thread-safe renderer at a time.
        rendererMutex.withLock {
            pdfRenderer.openPage(pageIndex).use { page ->
                val targetHeight = (renderWidth.toFloat() / page.width * page.height).toInt()
                val bitmap = createBitmap(renderWidth, targetHeight)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                // Cache the newly rendered bitmap before returning
                pageBitmapCache.put(pageIndex, bitmap)
                bitmap
            }
        }
    }

    /**
     * Closes the PdfRenderer and the underlying file descriptor.
     * Must be called when this provider is no longer needed (e.g., in ViewModel.onCleared()).
     */
    override fun close() {
        pdfRenderer.close()
        fileDescriptor?.close()
    }
}