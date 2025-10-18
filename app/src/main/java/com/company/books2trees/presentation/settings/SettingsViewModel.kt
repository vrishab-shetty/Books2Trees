package com.company.books2trees.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.books2trees.domain.model.UserData
import com.company.books2trees.domain.use_case.GetSignedInUserUseCase
import com.company.books2trees.domain.use_case.SignOutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SettingsViewState {
    data object Loading : SettingsViewState()
    data class Content(val data: UserData) : SettingsViewState()
    data class Error(val errorMessage: String?) : SettingsViewState()
}

sealed class SettingsViewEvent {
    data object NavigateToSignIn : SettingsViewEvent()
}

class SettingsViewModel(
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _userData = MutableStateFlow<SettingsViewState>(SettingsViewState.Loading)
    val userData = _userData.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SettingsViewEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            val result = getSignedInUserUseCase()
            if (result != null) {
                _userData.value = SettingsViewState.Content(result)
            } else {
                _userData.value = SettingsViewState.Error("User not found")
            }
        }
    }

    fun onSignOutClicked() {
        viewModelScope.launch {
            signOutUseCase()
            _navigationEvent.emit(SettingsViewEvent.NavigateToSignIn)
        }
    }
}