package com.company.books2trees.presentation.signin

import androidx.lifecycle.ViewModel
import com.company.books2trees.data.auth.SignInResult
import com.company.books2trees.data.auth.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow<SignInState>(SignInState.Pending)
    val state = _state.asStateFlow()


    fun onSignInResult(result: SignInResult) {
        _state.update {
            if (result.data != null) {
                SignInState.Content(result.data)
            } else {
                SignInState.Error(result.errorMessage!!)
            }
        }
    }

    fun resetState() {
        _state.update { SignInState.Pending }
    }

    fun onSignInData(userData: UserData) {
        _state.update {
            SignInState.Content(userData)
        }
    }
}