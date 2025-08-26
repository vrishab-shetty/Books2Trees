package com.company.books2trees.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.company.books2trees.databinding.ItemSearchResultBinding
import com.company.books2trees.ui.home.callbacks.OnBookClicked
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.models.BookModelDiffCallback
import com.company.books2trees.ui.search.viewholder.SearchResultHolder

class SearchResultAdapter(
    private val inflater: LayoutInflater,
    private val onItemClick: OnBookClicked,
) : ListAdapter<BookModel, SearchResultHolder>(BookModelDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchResultHolder(
        ItemSearchResultBinding.inflate(inflater, parent, false),
        onItemClick
    )

    override fun onBindViewHolder(holder: SearchResultHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }
}