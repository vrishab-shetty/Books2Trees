package com.company.books2trees.presentation.info

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.domain.use_case.AddPdfUseCase
import com.company.books2trees.domain.use_case.DeletePdfUseCase
import com.company.books2trees.domain.use_case.GetPdfsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class PdfListViewState {
    object Loading : PdfListViewState()
    data class Content(val list: List<PdfModel>) : PdfListViewState()
    data class Error(val errorMessage: String) : PdfListViewState()
}

class InfoViewModel(
    private val getPdfsUseCase: GetPdfsUseCase,
    private val addPdfUseCase: AddPdfUseCase,
    private val deletePdfUseCase: DeletePdfUseCase
) : ViewModel() {

    private val _pdfList = MutableStateFlow<PdfListViewState>(PdfListViewState.Loading)
    val pdfList: StateFlow<PdfListViewState> = _pdfList.asStateFlow()

    init {
        loadPdfList()
    }

    private fun loadPdfList() {
        viewModelScope.launch {
            getPdfsUseCase()
                .catch { exception ->
                    _pdfList.value =
                        PdfListViewState.Error(exception.message ?: "An unknown error occurred")
                }
                .collect { pdfs ->
                    _pdfList.value = PdfListViewState.Content(pdfs)
                }
        }
    }

    fun onUriReceived(uri: Uri) {
        viewModelScope.launch {
            addPdfUseCase(uri)
        }
    }

    fun onDeletePdf(model: PdfModel) {
        viewModelScope.launch {
            deletePdfUseCase(model)
        }
    }
}
