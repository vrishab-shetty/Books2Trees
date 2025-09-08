package com.company.books2trees.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.data.auth.UserData
import com.company.books2trees.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// This sealed class represents the state of the sign-in UI
sealed class SignInState {
    data object Pending : SignInState()
    data class Content(val data: UserData) : SignInState()
    data class Error(val errorMessage: String?) : SignInState()
}

class SignInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SignInState>(SignInState.Pending)
    val state = _state.asStateFlow()

    init {
        authRepository.user.value?.let { userData ->
            _state.update { SignInState.Content(userData) }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            val signInResult = authRepository.signIn()
            _state.update {
                if (signInResult.data != null) {
                    SignInState.Content(signInResult.data)
                } else {
                    SignInState.Error(signInResult.errorMessage)
                }
            }
        }
    }
}