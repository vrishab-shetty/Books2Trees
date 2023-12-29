package com.company.books2trees.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.company.books2trees.R
import com.company.books2trees.databinding.ItemHomePageListBinding
import com.company.books2trees.ui.common.AwardedBookViewType
import com.company.books2trees.ui.common.BookListAdapter
import com.company.books2trees.ui.common.DefaultBookViewType
import com.company.books2trees.ui.common.PopularBookViewType
import com.company.books2trees.ui.home.HomePageList
import com.company.books2trees.ui.home.HomeViewModel
import com.company.books2trees.ui.home.callbacks.OnBookClicked
import com.company.books2trees.ui.home.callbacks.OnBookLongPressed
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.profile.LibraryPageItem
import com.company.books2trees.ui.profile.LibraryPageItem.CategoryId
import com.company.books2trees.utils.UIHelper.popupMenuNoIcons

open class HomeParentItemAdapter(
    val items: MutableList<HomePageList>,
    val layoutInflater: LayoutInflater,
    val onClickListHeading: (text: String, list: List<BookModel>) -> Unit,
    val onClickBook: OnBookClicked,
    val viewModel: HomeViewModel
) : Adapter<ViewHolder>() {

    private var viewPool: RecyclerView.RecycledViewPool? = null

    init {
        viewPool = RecyclerView.RecycledViewPool()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ParentViewHolder(
            ItemHomePageListBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        ).apply { setViewPool(viewPool) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ParentViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    fun updateList(newList: List<HomePageList>, recyclerView: RecyclerView? = null) {

        val diffResult = DiffUtil.calculateDiff(
            HomePageList.DiffCallback(items, newList)
        )

        items.clear()
        items.addAll(newList)

        val delta = if (this@HomeParentItemAdapter is HomeParentItemAdapterPreview)
            headItems else 0;

        diffResult.dispatchUpdatesTo(object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position + delta, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position + delta, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition + delta, toPosition + delta)
            }

            override fun onChanged(_position: Int, count: Int, payload: Any?) {
                val position = _position + delta

                // Uses the update or bind instead of onCreateViewHolder -> bind
                recyclerView?.apply {
                    // this loops every viewHolder in the recycle view and checks the position
                    // to see if it is within the update range
                    val missingUpdates = (position until (position + count)).toMutableSet()
                    for (i in 0 until itemCount) {
                        val viewHolder = findViewHolderForAdapterPosition(i) ?: continue
                        if (viewHolder !is ParentViewHolder) continue

                        val absolutePosition = viewHolder.adapterPosition
                        if (absolutePosition >= position && absolutePosition < position + count) {
                            val expand = items.getOrNull(absolutePosition - delta) ?: continue
                            missingUpdates -= absolutePosition
                            if (viewHolder.title.text == resources.getString(expand.name)) {
                                viewHolder.update(expand)
                            } else {
                                viewHolder.bind(expand)
                            }
                        }
                    }

                    // just in case some item did not get updated
                    for (i in missingUpdates) {
                        notifyItemChanged(i, payload)
                    }
                } ?: run {
                    // in case we don't have a nice
                    notifyItemRangeChanged(position, count, payload)
                }
            }

        })

        // diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = items.size;

    override fun getItemId(position: Int): Long {
        return items[position].name.toLong()
    }

    inner class ParentViewHolder(private val binding: ItemHomePageListBinding) :
        ViewHolder(binding.root), OnBookLongPressed {

        private var recyclerView = binding.bookList
        val title: TextView = binding.books
        fun update(list: HomePageList) {

            val title = list.name

            if (recyclerView.adapter == null) {
                setUpAdapter(title)
            }

            (recyclerView.adapter as? BookListAdapter)?.apply {
                submitList(list.list)
            } ?: run {
                error("BookListAdapter is not initialized")
            }

        }

        fun bind(items: HomePageList) {

            val title = items.name

            setUpAdapter(title)

            binding.books.apply {
                text = resources.getString(title)
                setOnClickListener {
                    onClickListHeading(
                        (it as TextView).text.toString(),
                        (recyclerView.adapter as? BookListAdapter)?.let { adapter ->
                            adapter.currentList
                        } ?: error("BookListAdapter is not initialized")
                    )
                }
            }
            update(items)
        }


        private fun setUpAdapter(@StringRes title: Int) {

            recyclerView.adapter = when (title) {
                R.string.popular_books_caption -> BookListAdapter(
                    PopularBookViewType,
                    layoutInflater, onClickBook,
                    this
                )

                R.string.awarded_books_caption -> BookListAdapter(
                    AwardedBookViewType,
                    layoutInflater, onClickBook,
                    this
                )

                else -> BookListAdapter(
                    DefaultBookViewType,
                    layoutInflater, onClickBook,
                    null
                )

            }
        }

        fun setViewPool(viewPool: RecyclerView.RecycledViewPool?) {
            viewPool?.let { recyclerView.setRecycledViewPool(it) }
        }

        override fun showOptionMenu(model: BookModel, itemView: View) {
            val options = listOf(R.string.add_to_favorite)
            itemView.popupMenuNoIcons(options.mapIndexed { index, value ->
                Pair(index, value)
            }) {
                when(itemId) {
                    0 -> {
                        viewModel.insertToLibrary(model, CategoryId.PlanToRead)
                    }
                }
            }

        }
    }
}