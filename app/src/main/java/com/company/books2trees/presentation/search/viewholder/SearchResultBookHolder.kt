package com.company.books2trees.presentation.search.viewholder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.company.books2trees.databinding.ItemSearchResultBookBinding
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.home.callbacks.OnBookClicked

class SearchResultBookHolder(
    private val binding: ItemSearchResultBookBinding,
    private val onItemClick: OnBookClicked
) : ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            binding.model?.let { onItemClick.openBook(it) }
        }
    }

    fun bind(model: BookModel) {
        binding.model = model
        binding.executePendingBindings()
    }
}