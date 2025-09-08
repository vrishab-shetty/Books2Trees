package com.company.books2trees.presentation.signin

import com.company.books2trees.data.auth.UserData

sealed class SignInState {
    data object Pending : SignInState()
    data class Content(val userData: UserData) : SignInState()
    data class Error(val errorMessage: String): SignInState()
}
