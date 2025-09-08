package com.company.books2trees.presentation.utils

import androidx.lifecycle.Observer

class Event<out T>(private val content: T){

    private var hasBeenHandled: Boolean = false

    fun handle(handle: (T) -> Unit) {
        if(!hasBeenHandled) {
            hasBeenHandled = true
            handle(content)
        }
    }

    class EventObserver<T>(private val handler: (T) -> Unit) : Observer<Event<T>> {
        override fun onChanged(value: Event<T>) {
            value.handle(handler)
        }
    }
}