package com.company.books2trees.domain.use_case

import android.content.Context
import android.net.Uri
import com.company.books2trees.data.local.pdf.PdfPageProvider
import java.io.IOException

/**
 * Use Case for opening a PDF and creating a stateful PdfPageProvider.
 * This abstracts away the creation logic from the ViewModel.
 *
 * @param context The application context, needed to access the content resolver.
 */
class OpenPdfUseCase(private val context: Context) {
    /**
     * Executes the use case.
     * @param uri The URI of the PDF file to open.
     * @return A new instance of [PdfPageProvider].
     * @throws IOException if the file cannot be opened or the PDF is corrupt.
     */
    @Throws(IOException::class)
    operator fun invoke(uri: Uri): PdfPageProvider {
        return PdfPageProvider(context, uri)
    }
}
