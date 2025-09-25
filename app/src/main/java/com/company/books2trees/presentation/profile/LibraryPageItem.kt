package com.company.books2trees.presentation.profile

import androidx.recyclerview.widget.DiffUtil
import com.company.books2trees.domain.model.CategoryId
import com.company.books2trees.domain.model.LibraryBookModel

data class LibraryPageItem(
    val categoryId: CategoryId,
    val items: List<LibraryBookModel>
) {

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

