package com.company.books2trees.presentation.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.company.books2trees.R
import com.company.books2trees.data.local.model.LibraryItem
import com.company.books2trees.databinding.LibraryViewpagerBinding
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.home.callbacks.OnBookClicked
import com.company.books2trees.presentation.home.callbacks.OnBookLongPressed
import com.company.books2trees.presentation.profile.LibraryPageItem
import com.company.books2trees.presentation.profile.ProfileViewModel
import com.company.books2trees.presentation.profile.adapter.ViewPagerAdapter.PageViewHolder
import com.company.books2trees.presentation.utils.UIHelper.popupMenuNoIcons

class ViewPagerAdapter(
    private var items: MutableList<LibraryPageItem>,
    private val layoutInflater: LayoutInflater,
    private val onClickBook: OnBookClicked,
    private val viewModel: ProfileViewModel
) : RecyclerView.Adapter<PageViewHolder>() {

    private val TAG = "ViewPagerAdapter"
    private val privatePage: Int = 1

    private var pages = items.associate { it.categoryId to it.items }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder =
        PageViewHolder(
            LibraryViewpagerBinding.inflate(layoutInflater, parent, false)
        )

    override fun getItemCount(): Int = pages.size - privatePage


    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val item = pages[LibraryPageItem.CategoryId.entries[position + privatePage]]
        item?.let {
            holder.bind(it)
        }
    }

    fun updateItems(newList: List<LibraryPageItem>, viewPager: ViewPager2? = null) {

        val diffResult = DiffUtil.calculateDiff(LibraryPageItem.DiffCallback(items, newList))

        items.clear()
        items.addAll(newList)
        pages = items.associate { it.categoryId to it.items }
        val delta = privatePage

        diffResult.dispatchUpdatesTo(object : ListUpdateCallback {

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position - delta, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position - delta, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition - delta, toPosition - delta)
            }

            override fun onChanged(_position: Int, count: Int, payload: Any?) {
                val position = _position - delta
                // Uses the update or bind instead of onCreateViewHolder -> bind
                viewPager?.apply {
                    // this loops every viewHolder in the recycle view and checks the position
                    // to see if it is within the update range
                    val missingUpdates = (position until (position + count)).toMutableSet()
                    for (i in 0 until itemCount) {
                        val child = getChildAt(0) ?: continue
                        val viewHolder =
                            (child as RecyclerView).findViewHolderForAdapterPosition(i) ?: continue
                        val absolutePosition = viewHolder.adapterPosition

                        if (viewHolder !is PageViewHolder) {
                            // Need to add inorder to avoid unnecessary updates when viewHolder is not initialized
                            missingUpdates -= absolutePosition
                            continue
                        }


                        if (absolutePosition >= position && absolutePosition < position + count) {
                            val pages = items.getOrNull(absolutePosition + delta) ?: continue
                            missingUpdates -= absolutePosition
                            viewHolder.bind(pages.items)
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

    inner class PageViewHolder(
        private val binding: LibraryViewpagerBinding
    ) : ViewHolder(binding.root), OnBookLongPressed {


        fun bind(page: List<LibraryItem>) {
            binding.pageRecyclerview.apply {
                adapter?.let {
                    (adapter as? PageAdapter)?.updateList(page)
                } ?: run {
                    adapter = PageAdapter(page.toMutableList(), onClickBook, this@PageViewHolder)
                }
            }
        }

        override fun showOptionsMenu(model: BookModel, itemView: View) {
            val options = listOf(R.string.drop, R.string.on_hold, R.string.remove)

            itemView.popupMenuNoIcons(options.mapIndexed { index, value ->
                Pair(index, value)
            }) {
                val item =
                    pages[LibraryPageItem.CategoryId.entries[adapterPosition + privatePage]]?.find {
                        it.id == model.id
                    }

                if (item == null) {
                    return@popupMenuNoIcons
                }

                when (itemId) {
                    0 -> {
                        viewModel.onChangedCategory(item.apply {
                            categoryId = LibraryPageItem.CategoryId.Dropped
                        })
                    }

                    1 -> {
                        viewModel.onChangedCategory(item.apply {
                            categoryId = LibraryPageItem.CategoryId.OnHold
                        })
                    }

                    2 -> {
                        viewModel.onDeleteBookClicked(model.id)
                    }
                }
            }
        }
    }
}