package com.company.books2trees.data.repository

import com.company.books2trees.data.auth.GoogleAuthUiClient
import com.company.books2trees.data.auth.SignInResult
import com.company.books2trees.data.auth.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthRepository(
    private val googleAuthUiClient: GoogleAuthUiClient
) {

    private val _user = MutableStateFlow<UserData?>(null)
    val user = _user.asStateFlow()

    init {
        _user.value = googleAuthUiClient.getSignedInUser()
    }


    suspend fun signIn(): SignInResult {
        val result = googleAuthUiClient.signIn()
        // If sign-in was successful, update the global user state
        if (result.data != null) {
            _user.update { result.data }
        }
        return result
    }

    fun signOut() {
        googleAuthUiClient.signOut()
        _user.update { null }
    }
}