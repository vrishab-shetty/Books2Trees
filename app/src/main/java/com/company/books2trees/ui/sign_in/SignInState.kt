package com.company.books2trees.ui.sign_in

import com.company.books2trees.presentation.sign_in.UserData

sealed class SignInState {
    data object Pending : SignInState()
    data class Content(val userData: UserData) : SignInState()
    data class Error(val errorMessage: String): SignInState()
}
