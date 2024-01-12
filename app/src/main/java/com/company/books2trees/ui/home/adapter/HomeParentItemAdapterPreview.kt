package com.company.books2trees.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.company.books2trees.R
import com.company.books2trees.databinding.ItemHomePageListBinding
import com.company.books2trees.ui.common.BookListAdapter
import com.company.books2trees.ui.common.RecentBookViewType
import com.company.books2trees.ui.home.HomePageList
import com.company.books2trees.ui.home.HomeViewModel
import com.company.books2trees.ui.home.callbacks.OnBookClicked
import com.company.books2trees.ui.home.callbacks.OnBookLongPressed
import com.company.books2trees.ui.home.database.RecentItem
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.profile.LibraryPageItem
import com.company.books2trees.utils.UIHelper.popupMenuNoIcons
import com.company.books2trees.utils.observe

class HomeParentItemAdapterPreview(
    items: MutableList<HomePageList>,
    layoutInflater: LayoutInflater,
    onClickListHeading: (text: String, list: List<BookModel>) -> Unit,
    onClickBook: OnBookClicked,
    viewModel: HomeViewModel
) : HomeParentItemAdapter(
    items,
    layoutInflater,
    onClickListHeading,
    onClickBook,
    viewModel
) {

    companion object {
        private const val VIEW_TYPE_HEADER = 2
        private const val VIEW_TYPE_ITEM = 1
    }

    val headItems = 1;
    private var viewPool: RecycledViewPool? = null

    init {
        viewPool = RecycledViewPool().apply { setMaxRecycledViews(VIEW_TYPE_HEADER, 1) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                HeaderViewHolder(ItemHomePageListBinding.inflate(layoutInflater, parent, false))
                    .apply {  setViewPool(viewPool) }
            }

            VIEW_TYPE_ITEM -> {
                super.onCreateViewHolder(parent, viewType)
            }

            else -> error("Unhandled viewType=$viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {}
            else -> super.onBindViewHolder(holder, position - headItems)
        }
    }


    override fun getItemViewType(position: Int) = when (position) {
        0 -> VIEW_TYPE_HEADER
        else -> VIEW_TYPE_ITEM
    }

    override fun getItemCount() = items.size + headItems

    override fun getItemId(position: Int): Long {
        if (position == 0) return 0
        return super.getItemId(position - headItems)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.onViewAttachedToWindow()
            }

            else -> super.onViewAttachedToWindow(holder)
        }
    }

    inner class HeaderViewHolder(private val binding: ItemHomePageListBinding) :
        ViewHolder(binding.root), OnBookLongPressed {

        private var resumeListAdapter: BookListAdapter = BookListAdapter(
            RecentBookViewType,
            layoutInflater, onClickBook, this
        )

        private var resumeHolder = itemView.findViewById<TextView>(R.id.books)
        private var resumeRecyclerView: RecyclerView = itemView.findViewById(R.id.book_list)


        init {
            resumeRecyclerView.adapter = resumeListAdapter
            resumeHolder.apply {
                text = resources.getString(R.string.recent_books_caption)
                setOnClickListener {
                    onClickListHeading(
                        (it as TextView).text.toString(),
                        (resumeRecyclerView.adapter as? BookListAdapter)?.let { adapter ->
                            adapter.currentList
                        } ?: error("BookListAdapter is not initialized")
                    )
                }
            }
        }


        private fun updateResume(list: List<BookModel>) {
            resumeHolder.visibility = if(list.isNotEmpty()) View.VISIBLE else View.GONE
            resumeListAdapter.submitList(list)
        }

        fun onViewAttachedToWindow() {
            binding.root.findViewTreeLifecycleOwner()?.apply {
                observe(viewModel.recentList) {
                    updateResume(it.map(RecentItem::toBookModel))
                }
            }
        }

        fun setViewPool(viewPool: RecycledViewPool?) {
            viewPool?.let { resumeRecyclerView.setRecycledViewPool(it) }
        }

        override fun showOptionMenu(model: BookModel, itemView: View) {
            val options = listOf(R.string.add_to_favorite, R.string.remove)
            itemView.popupMenuNoIcons(options.mapIndexed { index, s -> Pair(index, s) }) {
                when (itemId) {
                    0 -> {
                        viewModel.insertToLibrary(model, LibraryPageItem.CategoryId.PlanToRead)
                    }

                    1 -> {
                        viewModel.onRemoveClicked(model)
                    }
                }
            }
        }


    }
}
