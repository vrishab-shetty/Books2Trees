package com.company.books2trees.ui.models

import androidx.recyclerview.widget.DiffUtil

data class PopularBookModel(
    override val id: String,
    val position: Int,
    override val name: String,
    val rating: Double,
    override val cover: String,
    override val url: String
) : BookModel(id, name, cover, url, "${rating.toString()} \u2605") {

    object BookModelDiffCallback : DiffUtil.ItemCallback<PopularBookModel>() {
        override fun areItemsTheSame(oldItem: PopularBookModel, newItem: PopularBookModel) =
            oldItem === newItem


        override fun areContentsTheSame(oldItem: PopularBookModel, newItem: PopularBookModel) =
            oldItem.id == newItem.id && oldItem.url == newItem.url
    }
}
