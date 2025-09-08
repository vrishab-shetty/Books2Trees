package com.company.books2trees.presentation.common

import com.company.books2trees.data.auth.UserData

interface OnViewProfile {
    fun getData(): UserData?
    fun signOut(callback: () -> Unit)
}