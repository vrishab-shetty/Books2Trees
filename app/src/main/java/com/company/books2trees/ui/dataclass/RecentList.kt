package com.company.books2trees.ui.dataclass

import java.util.Stack

class RecentList<T> private constructor(
    private val capacity: Int
) {
    private val items = Stack<T>()

    fun addItem(model: T) {

        if (items.contains(model)) {
            removeItem(model)
        }
        if (items.size == capacity) {
            items.removeFirst()
        }
        items.add(model)
    }

    fun removeItem(model: T) {
        items.remove(model)
    }

    fun getItems() = items.toList().reversed()

    fun getSize() = items.size

    companion object {
        @Synchronized
        operator fun <T> get(capacity: Int, list: List<T>): RecentList<T> {
            return RecentList<T>(capacity).also {
                list.forEach { item ->
                    it.addItem(item)
                }
            }
        }
    }

}