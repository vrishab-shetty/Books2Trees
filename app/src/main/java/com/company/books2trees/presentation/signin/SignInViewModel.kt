package com.company.books2trees.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.domain.model.UserData
import com.company.books2trees.domain.use_case.GetSignedInUserUseCase
import com.company.books2trees.domain.use_case.SignInWithGoogleTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// This sealed class represents the state of the sign-in UI
sealed class SignInState {
    data object Idle : SignInState()
    data object Loading : SignInState()
    data class Success(val data: UserData) : SignInState()
    data class Error(val errorMessage: String?) : SignInState()
}

class SignInViewModel(
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val signInWithGoogleTokenUseCase: SignInWithGoogleTokenUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SignInState>(SignInState.Idle)
    val state = _state.asStateFlow()

    init {
        checkActiveUserSession()
    }

    private fun checkActiveUserSession() {
        val user = getSignedInUserUseCase()
        if (user != null) {
            _state.update { SignInState.Success(user) }
        }
    }

    fun signInWithGoogleToken(token: String) {
        viewModelScope.launch {
            _state.update { SignInState.Loading }

            val result = signInWithGoogleTokenUseCase(token)
            _state.update {
                if (result.isSuccess) {
                    SignInState.Success(result.getOrThrow())
                } else {
                    SignInState.Error(result.exceptionOrNull()?.message)
                }
            }
        }
    }
}