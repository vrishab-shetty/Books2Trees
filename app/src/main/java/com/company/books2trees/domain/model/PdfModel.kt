package com.company.books2trees.domain.model

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import java.util.UUID

data class PdfModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    var thumbnail: String? = null,
    val info: String? = null,
    val uri: Uri
) {


    // ToDo: Move it to presentation layer
    object PdfModelDiffCallback : DiffUtil.ItemCallback<PdfModel>() {
        override fun areItemsTheSame(oldItem: PdfModel, newItem: PdfModel) =
            oldItem === newItem


        override fun areContentsTheSame(oldItem: PdfModel, newItem: PdfModel) =
            oldItem.name == newItem.name

    }
}