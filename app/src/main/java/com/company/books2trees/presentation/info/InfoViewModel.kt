package com.company.books2trees.presentation.info

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.data.repository.PdfRepository
import com.company.books2trees.domain.model.PdfModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PdfListViewState {
    object Loading : PdfListViewState()
    data class Content(val list: List<PdfModel>) : PdfListViewState()
    data class Error(val errorMessage: String) : PdfListViewState()
}

class InfoViewModel(application: Application) : AndroidViewModel(application) {

    private val _pdfList = MutableStateFlow<PdfListViewState>(PdfListViewState.Loading)
    val pdfList: StateFlow<PdfListViewState> = _pdfList.asStateFlow()

    init {
        _pdfList.value = PdfListViewState.Content(emptyList())
    }
    fun onUriReceived(uri: Uri) {
        viewModelScope.launch {
            PdfRepository.addPdf(getApplication(), uri)
        }
    }

    fun onDeletePdf(model: PdfModel) {
        viewModelScope.launch {
            PdfRepository.removePdf(getApplication(), model.id)
        }
    }

}
