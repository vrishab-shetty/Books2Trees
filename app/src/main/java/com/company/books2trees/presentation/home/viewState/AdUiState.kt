package com.company.books2trees.presentation.home.viewState

sealed interface AdUiState {
    object Idle : AdUiState
    object Loading : AdUiState
    data class RewardGranted(val url: String?) : AdUiState
    data class Error(val message: String) : AdUiState
}