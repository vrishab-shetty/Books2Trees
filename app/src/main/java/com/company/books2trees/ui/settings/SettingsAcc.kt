package com.company.books2trees.ui.settings

import android.os.Bundle
import android.view.View
import com.company.books2trees.R
import com.company.books2trees.ViewBindingFragment
import com.company.books2trees.databinding.SettingsAccountBinding
import com.company.books2trees.utils.UIHelper

class SettingsAcc : ViewBindingFragment<SettingsAccountBinding>(SettingsAccountBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useBinding {
            SettingsFragment.setUpToolbar(this, R.string.settings_account)
        }
    }
}