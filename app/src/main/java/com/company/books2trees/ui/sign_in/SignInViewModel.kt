package com.company.books2trees.ui.sign_in

import androidx.lifecycle.ViewModel
import com.company.books2trees.presentation.sign_in.SignInResult
import com.company.books2trees.presentation.sign_in.UserData
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