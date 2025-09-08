package com.company.books2trees.presentation.settings

import android.os.Bundle
import android.view.View
import com.company.books2trees.R
import com.company.books2trees.presentation.common.base.ViewBindingFragment
import com.company.books2trees.databinding.SettingsAccountBinding

class SettingsAcc : ViewBindingFragment<SettingsAccountBinding>(SettingsAccountBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useBinding {
            SettingsFragment.setUpToolbar(this, R.string.settings_account)
        }
    }
}