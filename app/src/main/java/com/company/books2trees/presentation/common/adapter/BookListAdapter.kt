package com.company.books2trees.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.common.BookHolder
import com.company.books2trees.presentation.home.callbacks.OnBookClicked
import com.company.books2trees.presentation.home.callbacks.OnBookLongPressed

class BookListAdapter(
    private val inflater: LayoutInflater,
    private val onItemClick: OnBookClicked,
    private val onItemLongClick: OnBookLongPressed?,
    @LayoutRes private val layoutId: Int
) : ListAdapter<BookModel, BookHolder>(BookModelDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, layoutId, parent, false
        )
        return BookHolder(
            binding,
            onItemClick,
            onItemLongClick
        )
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }
}