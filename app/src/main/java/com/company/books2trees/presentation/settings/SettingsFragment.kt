package com.company.books2trees.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.company.books2trees.R
import com.company.books2trees.databinding.FragmentSettingsBinding
import com.company.books2trees.domain.model.UserData
import com.company.books2trees.presentation.common.base.ViewBindingFragment
import com.company.books2trees.presentation.common.dialog.BasicAlertDialog
import com.company.books2trees.presentation.signin.SignInActivity
import com.company.books2trees.presentation.utils.UIHelper.navigateTo
import com.company.books2trees.presentation.utils.UIHelper.setUpToolbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment :
    ViewBindingFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val vm: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useBinding { binding ->
            binding.apply {
                settingsGeneral.setOnClickListener {
                    activity?.navigateTo(R.id.navigate_to_settings_general, null)
                }
                settingsUpdates.setOnClickListener {
                    activity?.navigateTo(R.id.navigate_to_settingsUpdates, null)
                }
            }
        }

        observeViewState()
        observeNavigationEvents()

    }

    private fun observeNavigationEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.navigationEvent.collect { event ->
                when (event) {
                    is SettingsViewEvent.NavigateToSignIn -> {
                        navigateToSignIn()
                    }
                }
            }

        }
    }

    private fun navigateToSignIn() {
        startActivity(Intent(activity, SignInActivity::class.java))
        requireActivity().finish()
    }

    private fun bindUserData(
        userData: UserData?
    ) {
        useBinding { binding ->
            binding.apply {
                data = userData
                signOutBtn.setOnClickListener {
                    val dialog = setUpDialog()
                    dialog.show()
                }
                executePendingBindings()
            }
        }
    }

    private fun setUpDialog() =
        BasicAlertDialog(
            requireActivity(),
            onPositiveButtonClick = {
                vm.onSignOutClicked()
            },
            positiveButtonText = R.string.sign_out,
            negativeButtonText = R.string.cancel_btn_caption,
            message = R.string.sign_out_dialog,
            title = R.string.sign_out
        )


    private fun observeViewState() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.userData.collect { viewState ->
                when (viewState) {
                    is SettingsViewState.Content -> {
                        val userData = viewState.data
                        bindUserData(userData)
                    }

                    is SettingsViewState.Error -> {
                        Toast.makeText(context, viewState.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        fun setUpToolbar(fragment: Fragment, @StringRes string: Int) {
            fragment.setUpToolbar(string)
        }
    }

}