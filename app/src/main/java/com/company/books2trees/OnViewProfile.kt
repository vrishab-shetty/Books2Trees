package com.company.books2trees

import com.company.books2trees.presentation.sign_in.UserData

interface OnViewProfile {
    fun getData(): UserData?
    fun signOut(callback: () -> Unit)
}