package com.company.books2trees.ui.models

import androidx.recyclerview.widget.DiffUtil

data class AwardedBookModel(
    override val id: String,
    val category: String,
    override val name: String,
    override val cover: String,
    override val url: String
) : BookModel(id, name, cover, url, category) {

    object BookModelDiffCallback : DiffUtil.ItemCallback<AwardedBookModel>() {

        override fun areItemsTheSame(oldItem: AwardedBookModel, newItem: AwardedBookModel) =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: AwardedBookModel, newItem: AwardedBookModel) =
            oldItem.id == newItem.id && oldItem.url == newItem.url

    }
}
