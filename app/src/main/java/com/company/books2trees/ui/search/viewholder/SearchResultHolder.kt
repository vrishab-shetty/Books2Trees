package com.company.books2trees.ui.search.viewholder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.company.books2trees.databinding.ItemSearchResultBinding
import com.company.books2trees.ui.home.callbacks.OnBookClicked
import com.company.books2trees.ui.models.BookModel

class SearchResultHolder(
    private val binding: ItemSearchResultBinding,
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