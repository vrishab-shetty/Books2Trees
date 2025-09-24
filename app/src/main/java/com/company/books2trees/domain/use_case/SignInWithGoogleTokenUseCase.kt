package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.UserData
import com.company.books2trees.domain.repository.AuthRepository

class SignInWithGoogleTokenUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(token: String): Result<UserData> {
        return repository.signInWithGoogleToken(token)
    }
}