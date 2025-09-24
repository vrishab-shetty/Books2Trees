package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.UserData
import com.company.books2trees.domain.repository.AuthRepository

class GetSignedInUserUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(): UserData? {
        return repository.getSignedInUser()
    }
}