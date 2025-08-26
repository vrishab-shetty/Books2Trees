package com.company.books2trees.ui.models

import androidx.recyclerview.widget.DiffUtil

object BookModelDiffCallback : DiffUtil.ItemCallback<BookModel>() {
    override fun areItemsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
        return oldItem.id == newItem.id && oldItem.url == newItem.url
    }
}