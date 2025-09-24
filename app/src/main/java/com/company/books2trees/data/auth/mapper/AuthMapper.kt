package com.company.books2trees.data.auth.mapper

import com.company.books2trees.domain.model.UserData


fun com.google.firebase.auth.FirebaseUser.toUserData(): UserData = UserData(
    userId = uid,
    username = displayName,
    profilePictureUrl = photoUrl?.toString()
)