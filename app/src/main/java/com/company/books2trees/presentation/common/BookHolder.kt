package com.company.books2trees.presentation.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.BR
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.home.callbacks.OnBookClicked
import com.company.books2trees.presentation.home.callbacks.OnBookLongPressed

open class BookHolder(
    private val binding: ViewDataBinding,
    private val onItemClick: OnBookClicked,
    private val onItemPressed: OnBookLongPressed?
) : RecyclerView.ViewHolder(binding.root) {

    private var currentModel: BookModel? = null

    init {
        binding.root.setOnClickListener {
            currentModel?.let { onItemClick.openBook(it) }
        }

        binding.root.setOnLongClickListener { itemView ->
            currentModel?.let {
                onItemPressed?.showOptionsMenu(it, itemView)
            }
            onItemPressed != null
        }
    }

    fun bind(model: BookModel) {
        this.currentModel = model
        binding.setVariable(BR.model, model)
        binding.executePendingBindings()
    }
}