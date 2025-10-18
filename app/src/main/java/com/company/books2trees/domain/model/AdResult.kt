package com.company.books2trees.domain.model

sealed class AdResult {
    object Success : AdResult()
    object Failed : AdResult()
}