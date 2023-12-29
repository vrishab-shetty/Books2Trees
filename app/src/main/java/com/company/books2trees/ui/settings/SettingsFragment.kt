package com.company.books2trees.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.company.books2trees.OnViewProfile
import com.company.books2trees.R
import com.company.books2trees.ViewBindingFragment
import com.company.books2trees.databinding.FragmentSettingsBinding
import com.company.books2trees.ui.sign_in.SignInActivity
import com.company.books2trees.utils.UIHelper.navigateTo
import com.company.books2trees.utils.UIHelper.setUpToolbar


class SettingsFragment :
    ViewBindingFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useBinding { binding ->

            binding.apply {
                settingsGeneral.setOnClickListener {
                    activity?.navigateTo(R.id.navigate_to_settings_general, null)
                }
                settingsGeneral.setOnClickListener {
                    activity?.navigateTo(R.id.navigate_to_settings_general, null)
                }
                settingsHelp.setOnClickListener {
                    activity?.navigateTo(R.id.navigate_to_settingsHelpSupport, null)
                }
                settingsUpdates.setOnClickListener {
                    activity?.navigateTo(R.id.navigate_to_settingsUpdates, null)
                }
            }

            requireActivity().let { activity ->
                if (activity is OnViewProfile) {
                    val userData = activity.getData()

                    binding.apply {
                        data = userData
                        signOutBtn.setOnClickListener {
                            activity.signOut {
                                startActivity(Intent(activity, SignInActivity::class.java))
                                activity.finish()
                            }
                        }
                        executePendingBindings()
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