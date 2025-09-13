package com.company.books2trees.presentation.profile

import androidx.recyclerview.widget.DiffUtil
import com.company.books2trees.data.local.model.LibraryItem

data class LibraryPageItem(
    val categoryId: CategoryId,
    val items: List<LibraryItem>
) {
    enum class CategoryId {
        None,
        Reading,
        PlanToRead,
        Dropped,
        Completed,
        OnHold
    }

    class DiffCallback(
        private val oldList: List<LibraryPageItem>,
        private val newList: List<LibraryPageItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].categoryId == newList[newItemPosition].categoryId

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

    }


}

