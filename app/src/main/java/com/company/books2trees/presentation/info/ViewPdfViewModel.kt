package com.company.books2trees.presentation.info

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.company.books2trees.data.repository.PdfRepository
import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.presentation.common.PdfViewState
import kotlinx.coroutines.launch


class ViewPdfViewModel(application: Application) : AndroidViewModel(application) {

    private var model: PdfModel? = null
    private var _renderer = MutableLiveData<PdfViewState>()
    val renderer: LiveData<PdfViewState>
        get() = _renderer

    fun getModel(id: String) = model ?: PdfRepository.findItemById(id).also {
        model = it
        getPdfRenderer()
    }

    private fun getPdfRenderer() {
        if (model != null) {
            viewModelScope.launch {
                _renderer.postValue(PdfRepository.getRenderer(getApplication(), model!!))
            }
        }

    }

    override fun onCleared() {
        renderer.value?.let {
            if (it is PdfViewState.Content) {
                it.renderer.close()
            }
        }
        super.onCleared()
    }
}