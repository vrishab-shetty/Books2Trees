package com.company.books2trees.presentation.common.adapter

import androidx.recyclerview.widget.DiffUtil
import com.company.books2trees.domain.model.BookModel

object BookModelDiffCallback : DiffUtil.ItemCallback<BookModel>() {
    override fun areItemsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
        return oldItem.id == newItem.id && oldItem.url == newItem.url
    }
}