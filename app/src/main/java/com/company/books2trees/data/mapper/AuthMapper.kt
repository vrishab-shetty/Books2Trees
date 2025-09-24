package com.company.books2trees.data.mapper

import com.company.books2trees.domain.model.UserData
import com.google.firebase.auth.FirebaseUser


fun FirebaseUser.toUserData(): UserData = UserData(
    userId = uid,
    username = displayName,
    profilePictureUrl = photoUrl?.toString()
)