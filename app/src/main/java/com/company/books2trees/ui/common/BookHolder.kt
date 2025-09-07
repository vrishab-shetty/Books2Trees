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

    private var currentModel: BookModel? = null

    init {
        binding.root.setOnClickListener {
            currentModel?.let { onItemClick.openBook(it) }
        }

        binding.root.setOnLongClickListener {itemView ->
            currentModel?.let {
                onItemPressed?.showOptionsMenu(it, itemView)
            }
            onItemPressed != null
        }
    }

    fun bind(model: BookModel) {
        this.currentModel = model
        binding.model = model
        binding.executePendingBindings()
    }
}