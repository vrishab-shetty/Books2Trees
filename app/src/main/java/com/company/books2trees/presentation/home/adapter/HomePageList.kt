package com.company.books2trees.presentation.home.adapter

import com.company.books2trees.domain.model.BookModel

data class HomePageList(
    var list: List<BookModel>,
    val name: Int
) {

    companion object {
        fun getListFromMap(map: Map<Int, List<BookModel>>): List<HomePageList> {
            val items = mutableListOf<HomePageList>()

            map.forEach { entry ->
                items.add(HomePageList(entry.value, entry.key))
            }

            return items
        }
    }
}