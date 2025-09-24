package com.company.books2trees.data.auth.impl

import com.company.books2trees.data.auth.mapper.toUserData
import com.company.books2trees.domain.model.UserData
import com.company.books2trees.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class AuthRepositoryImpl(
    private val auth: FirebaseAuth = Firebase.auth
) : AuthRepository {

    override suspend fun signInWithGoogleToken(token: String): Result<UserData> {
        return try {
            val credential = GoogleAuthProvider.getCredential(token, null)
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user

            if (user == null) {
                Result.failure(Exception("Firebase authentication failed: user is null."))
            } else {
                Result.success(user.toUserData())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Re-throw cancellation exceptions to let the coroutine handle them
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    override fun getSignedInUser(): UserData? {
        return auth.currentUser?.toUserData()
    }
}