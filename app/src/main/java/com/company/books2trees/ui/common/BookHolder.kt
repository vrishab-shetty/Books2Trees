package com.company.books2trees.ui.common

import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.databinding.ItemBookBinding
import com.company.books2trees.ui.home.callbacks.OnBookClicked
import com.company.books2trees.ui.home.callbacks.OnBookLongPressed
import com.company.books2trees.ui.models.BookModel

open class BookHolder(
    private val binding: ItemBookBinding,
    private val onItemClick: OnBookClicked,
    private val onItemPressed: OnBookLongPressed?
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            binding.model?.let { onItemClick.openBook(it) }
        }

        binding.root.setOnLongClickListener {itemView ->
            binding.model?.let {
                onItemPressed?.showOptionMenu(it, itemView)
            }
            onItemPressed != null
        }
    }

    fun bind(model: BookModel) {
        binding.model = model
        binding.executePendingBindings()
    }
}