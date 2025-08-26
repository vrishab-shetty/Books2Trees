package com.company.books2trees.ui.models

import androidx.recyclerview.widget.DiffUtil

open class BookModel(
    open val id: String,
    open val name: String,
    open val cover: String?,
    open val url: String?,
    val extras: String?
) {

    override fun equals(other: Any?): Boolean {
        return if (this === other) {
            true
        } else if (other !is BookModel) {
            false
        } else {
            this.id == other.id && this.name == other.name
        }
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + cover.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (extras?.hashCode() ?: 0)
        return result
    }

    object BookModelDiffCallback : DiffUtil.ItemCallback<BookModel>() {

        override fun areItemsTheSame(oldItem: BookModel, newItem: BookModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BookModel, newItem: BookModel) =
            oldItem.id == newItem.id && oldItem.url == newItem.url

    }

}
