package com.company.books2trees.presentation.profile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.company.books2trees.data.local.database.model.LibraryBookEntity
import com.company.books2trees.data.mapper.toBookModelFromLibrary
import com.company.books2trees.databinding.ItemLibraryBinding
import com.company.books2trees.presentation.home.callbacks.OnBookClicked
import com.company.books2trees.presentation.home.callbacks.OnBookLongPressed
import com.company.books2trees.presentation.utils.DiffAdapter

class PageAdapter(
    private var pages: MutableList<LibraryBookEntity>,
    private val onItemClick: OnBookClicked,
    private val onItemLongClick: OnBookLongPressed,
) : DiffAdapter<LibraryBookEntity>(pages, { first, second -> first.id == second.id }) {


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

    @SuppressLint("NotifyDataSetChanged")
    fun sortData() {
        pages.sortBy { it.title }
        notifyDataSetChanged()
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

        fun bind(page: LibraryBookEntity) {
            binding.model = page.toBookModelFromLibrary()
            binding.executePendingBindings()
        }
    }
}