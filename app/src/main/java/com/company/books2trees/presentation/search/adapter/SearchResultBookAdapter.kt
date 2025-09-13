package com.company.books2trees.presentation.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.company.books2trees.databinding.ItemSearchResultBookBinding
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.common.adapter.BookModelDiffCallback
import com.company.books2trees.presentation.home.callbacks.OnBookClicked
import com.company.books2trees.presentation.search.viewholder.SearchResultBookHolder

class SearchResultBookAdapter(
    private val inflater: LayoutInflater,
    private val onItemClick: OnBookClicked,
) : ListAdapter<BookModel, SearchResultBookHolder>(BookModelDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchResultBookHolder(
        ItemSearchResultBookBinding.inflate(inflater, parent, false),
        onItemClick
    )

    override fun onBindViewHolder(holder: SearchResultBookHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }
}