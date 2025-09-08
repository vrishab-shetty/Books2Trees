package com.company.books2trees.presentation.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.company.books2trees.domain.model.BookModel

data class HomePageList(
    var list: List<BookModel>,
    val name: Int
) {

    companion object {
        fun getListFromMap(map: Map<Int, List<BookModel>>): List<HomePageList> {
            val items = mutableListOf<HomePageList>()

            map.forEach { entry ->
                items.add(HomePageList(entry.value, entry.key));
            }

            return items
        }
    }


    class DiffCallback(
        private val oldList: List<HomePageList>,
        private val newList: List<HomePageList>
    ) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].name == newList[newItemPosition].name

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].list == newList[newItemPosition].list
    }
}