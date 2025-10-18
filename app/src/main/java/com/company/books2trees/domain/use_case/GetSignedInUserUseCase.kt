package com.company.books2trees.domain.use_case

import com.company.books2trees.domain.model.UserData
import com.company.books2trees.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSignedInUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): UserData? = withContext(Dispatchers.IO){
        repository.getSignedInUser()
    }
}