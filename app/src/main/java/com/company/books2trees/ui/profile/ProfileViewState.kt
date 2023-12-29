package com.company.books2trees.ui.profile

sealed class ProfileViewState {
    data object Loading : ProfileViewState()
    data class Content(val list: List<LibraryPageItem>) : ProfileViewState()
    data class Error(val throwable: Throwable) : ProfileViewState()
}