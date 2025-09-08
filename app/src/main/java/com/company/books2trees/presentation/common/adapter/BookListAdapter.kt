package com.company.books2trees.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.company.books2trees.databinding.ItemBookBinding
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.common.BookHolder
import com.company.books2trees.presentation.home.callbacks.OnBookClicked
import com.company.books2trees.presentation.home.callbacks.OnBookLongPressed

class BookListAdapter(
    private val inflater: LayoutInflater,
    private val onItemClick: OnBookClicked,
    private val onItemLongClick: OnBookLongPressed?,
) : ListAdapter<BookModel, BookHolder>(BookModelDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BookHolder(
        ItemBookBinding.inflate(inflater, parent, false),
        onItemClick,
        onItemLongClick
    )

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }
}