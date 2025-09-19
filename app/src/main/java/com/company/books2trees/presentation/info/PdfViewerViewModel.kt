package com.company.books2trees.presentation.info

import android.graphics.Bitmap
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.data.local.PdfPageProvider
import com.company.books2trees.domain.use_case.ClosePdfUseCase
import com.company.books2trees.domain.use_case.GetPdfPageBitmapUseCase
import com.company.books2trees.domain.use_case.OpenPdfUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed class PdfViewerUiState {
    object Loading : PdfViewerUiState()
    data class Content(val pageCount: Int) : PdfViewerUiState()
    data class Error(val message: String) : PdfViewerUiState()
}

class PdfViewerViewModel(
    private val openPdfUseCase: OpenPdfUseCase,
    private val getPdfPageBitmapUseCase: GetPdfPageBitmapUseCase,
    private val closePdfUseCase: ClosePdfUseCase,
    private val pdfUriString: String
) : ViewModel() {

    private var pageProvider: PdfPageProvider? = null

    private val _uiState = MutableStateFlow<PdfViewerUiState>(PdfViewerUiState.Loading)
    val uiState: StateFlow<PdfViewerUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                // Delegate the creation logic to the use case
                pageProvider = openPdfUseCase(pdfUriString.toUri())
                _uiState.update {
                    PdfViewerUiState.Content(pageProvider?.pageCount ?: 0)
                }
            } catch (e: IOException) {
                _uiState.update {
                    PdfViewerUiState.Error(e.message ?: "Couldn't load pdf file")
                }
            }
        }
    }

    suspend fun getPageBitmap(pageIndex: Int, renderWidth: Int): Bitmap? {
        if (_uiState.value !is PdfViewerUiState.Content) return null
        return getPdfPageBitmapUseCase(pageProvider, pageIndex, renderWidth)
    }

    override fun onCleared() {
        super.onCleared()
        closePdfUseCase(pageProvider)
    }
}

