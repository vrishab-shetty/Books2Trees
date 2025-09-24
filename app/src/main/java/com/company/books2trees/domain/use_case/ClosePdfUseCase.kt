package com.company.books2trees.domain.use_case

import com.company.books2trees.data.local.pdf.PdfPageProvider

/**
 * Use Case for closing an active PdfPageProvider and releasing its resources.
 */
class ClosePdfUseCase {
    /**
     * Executes the use case.
     * @param pageProvider The provider instance to close.
     */
    operator fun invoke(pageProvider: PdfPageProvider?) {
        pageProvider?.close()
    }
}
