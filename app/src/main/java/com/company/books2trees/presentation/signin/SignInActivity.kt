package com.company.books2trees.presentation.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.company.books2trees.MainActivity
import com.company.books2trees.R
import com.company.books2trees.databinding.ActivitySignInBinding
import com.company.books2trees.presentation.utils.collectLatestLifecycleFlow
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity() {

    private val vm: SignInViewModel by viewModel()
    private lateinit var binding: ActivitySignInBinding
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        credentialManager = CredentialManager.create(this)

        setUpSignInButton()
        observeViewState()

    }

    private fun observeViewState() {
        collectLatestLifecycleFlow(vm.state) { state ->
            binding.signInBtn.isEnabled = state !is SignInState.Loading
            binding.progressBar.visibility =
                if (state is SignInState.Loading) View.VISIBLE else View.GONE

            when (state) {
                is SignInState.Idle -> {
                }

                is SignInState.Loading -> {
                }

                is SignInState.Success -> {
                    Toast.makeText(
                        this,
                        "Welcome, ${state.data.username ?: "User"}",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is SignInState.Error -> {
                    Toast.makeText(
                        this,
                        "Sign In Unsuccessful: ${state.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setUpSignInButton() {
        binding.signInBtn.setOnClickListener {
            lifecycleScope.launch {
                val request = buildGetCredentialRequest()

                try {
                    // This single call shows the UI and waits for the user's choice.
                    val result = credentialManager.getCredential(
                        context = this@SignInActivity,
                        request = request
                    )

                    // If successful, extract the token and pass it to the ViewModel.
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(result.credential.data)
                    val googleIdToken = googleIdTokenCredential.idToken

                    vm.signInWithGoogleToken(googleIdToken)

                } catch (e: GetCredentialException) {
                    // Handle failures, such as the user canceling the dialog.
                    e.printStackTrace()
                    Toast.makeText(
                        applicationContext,
                        "Sign-in failed. Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    private fun buildGetCredentialRequest(): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // Show all Google accounts
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }
}

