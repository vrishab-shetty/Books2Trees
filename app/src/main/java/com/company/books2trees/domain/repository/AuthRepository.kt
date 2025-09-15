package com.company.books2trees.domain.repository

import com.company.books2trees.domain.model.UserData

interface AuthRepository {

    suspend fun signInWithGoogleToken(token: String): Result<UserData>

    suspend fun signOut()

    fun getSignedInUser(): UserData?
}