package com.company.books2trees.presentation.info

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.company.books2trees.data.repository.PdfRepository
import com.company.books2trees.domain.model.PdfModel
import kotlinx.coroutines.launch

class InfoViewModel(application: Application) : AndroidViewModel(application) {

    val pdfList: LiveData<List<PdfModel>> = PdfRepository.getItems()

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
