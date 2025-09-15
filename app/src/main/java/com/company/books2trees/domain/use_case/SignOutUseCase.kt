package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.repository.AuthRepository

class SignOutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        return repository.signOut()
    }
}