package com.company.books2trees.presentation.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.company.books2trees.data.mapper.toBookModelFromLibraryModel
import com.company.books2trees.databinding.ItemLibraryBinding
import com.company.books2trees.domain.model.LibraryBookModel
import com.company.books2trees.presentation.home.callbacks.OnBookClicked
import com.company.books2trees.presentation.home.callbacks.OnBookLongPressed
import com.company.books2trees.presentation.utils.DiffAdapter

class PageAdapter(
    private var pages: MutableList<LibraryBookModel>,
    private val onItemClick: OnBookClicked,
    private val onItemLongClick: OnBookLongPressed,
) : DiffAdapter<LibraryBookModel>(pages, { first, second -> first.id == second.id }) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        LibraryItemViewHolder(
            ItemLibraryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is LibraryItemViewHolder -> {
                holder.bind(pages[position])
            }
        }
    }


    inner class LibraryItemViewHolder(
        private val binding: ItemLibraryBinding
    ) : ViewHolder(binding.root) {

        init {

            binding.root.apply {
                setOnClickListener {
                    binding.model?.let { onItemClick.openBook(it) }
                }
                setOnLongClickListener {
                    binding.model?.let { onItemLongClick.showOptionsMenu(it, itemView) }
                    true
                }
            }
        }

        fun bind(page: LibraryBookModel) {
            binding.model = page.toBookModelFromLibraryModel()
            binding.executePendingBindings()
        }
    }
}