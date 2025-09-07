package com.company.books2trees.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.R
import com.company.books2trees.databinding.ItemHomePageListBinding
import com.company.books2trees.ui.common.BookListAdapter
import com.company.books2trees.ui.home.callbacks.OnBookClicked
import com.company.books2trees.ui.home.callbacks.OnBookLongPressed
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.utils.UIHelper.popupMenuNoIcons

class HomeAdapter(
    private val layoutInflater: LayoutInflater,
    private val listener: Listener,
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
) : ListAdapter<HomePageListItem, RecyclerView.ViewHolder>(DiffCallback()) {

    interface Listener: OnBookClicked {
        fun onListHeadingClicked(title: String, books: List<BookModel>)
        fun onBookAddToFavorites(book: BookModel)
        fun onBookRemove(book: BookModel)
    }

    companion object {
        private const val VIEW_TYPE_RECENT_HEADER = 1
        private const val VIEW_TYPE_BOOK_LIST = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomePageListItem.BookList -> VIEW_TYPE_BOOK_LIST
            is HomePageListItem.RecentBooks -> VIEW_TYPE_RECENT_HEADER
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val binding = ItemHomePageListBinding.inflate(layoutInflater, parent, false)

        return when (viewType) {
            VIEW_TYPE_RECENT_HEADER -> HeaderViewHolder(binding)
            VIEW_TYPE_BOOK_LIST -> ParentViewHolder(binding)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (val item = getItem(position)) {
            is HomePageListItem.BookList -> (holder as ParentViewHolder).bind(item)
            is HomePageListItem.RecentBooks -> (holder as HeaderViewHolder).bind(item)
        }
    }

    inner class HeaderViewHolder(private val binding: ItemHomePageListBinding) : RecyclerView.ViewHolder(
        binding.root
    ), OnBookLongPressed {
        private val bookListAdapter =
            BookListAdapter(layoutInflater, listener, this)

        init {
            binding.bookList.adapter = bookListAdapter
            binding.bookList.setRecycledViewPool(viewPool)
        }

        fun bind(item: HomePageListItem.RecentBooks) {
            binding.books.text = binding.root.context.getString(R.string.recent_books_caption)
            bookListAdapter.submitList(item.books)
            binding.books.setOnClickListener {
                listener.onListHeadingClicked(binding.books.text.toString(), item.books)
            }
        }

        override fun showOptionsMenu(model: BookModel, itemView: View) {
            val options = listOf(R.string.add_to_favorite, R.string.remove)
            itemView.popupMenuNoIcons(options.mapIndexed { index, s -> Pair(index, s) }) {
                when (itemId) {
                    0 -> listener.onBookAddToFavorites(model)
                    1 -> listener.onBookRemove(model)
                }
            }
        }
    }

    inner class ParentViewHolder(private val binding: ItemHomePageListBinding) : RecyclerView.ViewHolder(
        binding.root
    ), OnBookLongPressed {
        private val bookListAdapter = BookListAdapter(layoutInflater, listener, this)

        init {
            binding.bookList.adapter = bookListAdapter
            binding.bookList.setRecycledViewPool(viewPool)
        }

        fun bind(item: HomePageListItem.BookList) {
            val title = binding.root.context.getString(item.list.name)
            binding.books.text = title
            bookListAdapter.submitList(item.list.list)
            binding.books.setOnClickListener {
                listener.onListHeadingClicked(title, item.list.list)
            }
        }

        override fun showOptionsMenu(model: BookModel, itemView: View) {
            val options = listOf(R.string.add_to_favorite)
            itemView.popupMenuNoIcons(options.mapIndexed { index, value -> Pair(index, value)}) {
                when (itemId) {
                    0 -> listener.onBookAddToFavorites(model)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HomePageListItem>() {
        override fun areItemsTheSame(
            oldItem: HomePageListItem,
            newItem: HomePageListItem
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: HomePageListItem,
            newItem: HomePageListItem
        ) = oldItem == newItem
    }


}