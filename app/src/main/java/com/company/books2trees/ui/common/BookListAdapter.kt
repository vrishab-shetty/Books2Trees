package com.company.books2trees.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.databinding.ItemBookBinding
import com.company.books2trees.databinding.ItemSearchResultBinding
import com.company.books2trees.ui.home.callbacks.OnBookClicked
import com.company.books2trees.ui.home.callbacks.OnBookLongPressed
import com.company.books2trees.ui.models.AwardedBookModel
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.models.PopularBookModel
import com.company.books2trees.ui.home.viewholder.AwardedBookHolder
import com.company.books2trees.ui.home.viewholder.PopularBookHolder
import com.company.books2trees.ui.home.viewholder.RecentBookHolder
import com.company.books2trees.ui.search.viewholder.SearchResultHolder


const val PopularBookViewType = 0
const val AwardedBookViewType = 1
const val RecentBookViewType = 2
const val SearchResultViewType = 3
const val DefaultBookViewType = Int.MAX_VALUE

class BookListAdapter(
    private val viewType: Int,
    private val inflater: LayoutInflater,
    private val onItemClick: OnBookClicked,
    private val onItemLongClick: OnBookLongPressed?,
) : ListAdapter<BookModel, RecyclerView.ViewHolder>(BookModel.BookModelDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            PopularBookViewType -> PopularBookHolder(
                ItemBookBinding.inflate(inflater, parent, false),
                onItemClick,
                onItemLongClick!!
            )

            AwardedBookViewType -> AwardedBookHolder(
                ItemBookBinding.inflate(inflater, parent, false),
                onItemClick,
                onItemLongClick!!
            )

            SearchResultViewType -> SearchResultHolder(
                ItemSearchResultBinding.inflate(inflater, parent, false),
                onItemClick
            )

            RecentBookViewType -> RecentBookHolder(
                ItemBookBinding.inflate(inflater, parent, false),
                onItemClick,
                onItemLongClick!!
            )

            else -> BookHolder(
                ItemBookBinding.inflate(inflater, parent, false),
                onItemClick,
                null
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = getItem(position)

        if (holder is BookHolder) {
            holder.bind(model)
        } else if (holder is SearchResultHolder) {
            holder.bind(model)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return viewType;
    }


}
