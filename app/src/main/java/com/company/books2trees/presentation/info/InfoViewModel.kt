package com.company.books2trees.presentation.info

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.data.repository.PdfRepository
import com.company.books2trees.domain.model.PdfModel
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

// ToDo: Should Depend on UseCases
class InfoViewModel(private val pdfRepository: PdfRepository) : ViewModel() {

    private val _pdfList = MutableStateFlow<PdfListViewState>(PdfListViewState.Loading)
    val pdfList: StateFlow<PdfListViewState> = _pdfList.asStateFlow()

    init {
        observePdfList()
    }

    private fun observePdfList() {
        viewModelScope.launch {
            // Assuming pdfRepository.getItems() returns a Flow<List<PdfModel>>.
            // This flow will automatically emit a new list whenever the underlying data changes.
            pdfRepository.getItems()
                .catch { exception ->
                    // If the flow throws an error, update the state to show it.
                    _pdfList.value =
                        PdfListViewState.Error(exception.message ?: "An unknown error occurred")
                }
                .collect { pdfs ->
                    // When the flow emits a new list, update the UI state.
                    _pdfList.value = PdfListViewState.Content(pdfs)
                }
        }
    }

    fun onUriReceived(uri: Uri) {
        viewModelScope.launch {
            pdfRepository.addPdf(uri)
        }
    }

    fun onDeletePdf(model: PdfModel) {
        viewModelScope.launch {
            pdfRepository.removePdf(model.id)
        }
    }

}
