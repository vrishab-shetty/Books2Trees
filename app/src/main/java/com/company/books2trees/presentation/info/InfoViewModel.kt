package com.company.books2trees.presentation.info

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.data.repository.PdfRepository
import kotlinx.coroutines.launch

class InfoViewModel(application: Application) : AndroidViewModel(application) {

    val pdfList: LiveData<List<PdfModel>> = PdfRepository.getItems()

    fun onUriReceived(uri: Uri) {
        Log.i("TAG", "onUriReceived: ${uri.path}")
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
