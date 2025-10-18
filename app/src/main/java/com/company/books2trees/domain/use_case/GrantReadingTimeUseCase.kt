package com.company.books2trees.domain.use_case

import android.app.Activity
import com.company.books2trees.domain.model.AdResult
import com.company.books2trees.domain.repository.AdRepository

class GrantReadingTimeUseCase(
    private val adRepository: AdRepository
) {
    suspend operator fun invoke(activity: Activity): Boolean {
        return when (adRepository.showRewardedAd(activity)) {
            is AdResult.Success -> {
                true
            }

            is AdResult.Failed -> {
                false
            }
        }
    }
}