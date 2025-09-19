package com.company.books2trees.domain.use_case

import android.graphics.Bitmap
import com.company.books2trees.data.local.PdfPageProvider

/**
 * Use Case for rendering a single PDF page from an active PdfPageProvider.
 */
class GetPdfPageBitmapUseCase {
    /**
     * Executes the use case.
     * @param pageProvider The active provider for the open PDF.
     * @param pageIndex The index of the page to render.
     * @param renderWidth The target width for the rendered bitmap.
     * @return A [Bitmap] of the rendered page, or null if the provider is null.
     */
    suspend operator fun invoke(
        pageProvider: PdfPageProvider?,
        pageIndex: Int,
        renderWidth: Int
    ): Bitmap? {
        // It could contain more complex logic in the future (e.g., analytics).
        return pageProvider?.getPage(pageIndex, renderWidth)
    }
}
