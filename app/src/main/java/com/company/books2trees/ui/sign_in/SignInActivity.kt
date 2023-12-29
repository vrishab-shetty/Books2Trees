package com.company.books2trees.ui.sign_in

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.company.books2trees.MainActivity
import com.company.books2trees.databinding.ActivitySignInBinding
import com.company.books2trees.presentation.sign_in.GoogleAuthUiClient
import com.company.books2trees.utils.collectLatestLifecycleFlow
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {
    private val vm: SignInViewModel by viewModels()
    private lateinit var binding: ActivitySignInBinding

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = googleAuthUiClient.signInResultFromIntent(
                        intent = result.data ?: return@launch
                    )
                    Toast.makeText(this@SignInActivity, "Signed In Successfully", Toast.LENGTH_SHORT).show()
                    vm.onSignInResult(signInResult)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleAuthUiClient.getSignedInUser()?.run {
            Toast.makeText(this@SignInActivity, "Welcome ${this.username}", Toast.LENGTH_SHORT).show()
            vm.onSignInData(this)
        }

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInBtn.setOnClickListener {
            lifecycleScope.launch {
                googleAuthUiClient.getSignedInUser()?.run {
                    vm.onSignInData(this)
                } ?: run {
                    val signInIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            }
        }

        collectLatestLifecycleFlow(vm.state) { state ->
            when (state) {
                is SignInState.Pending -> {

                }

                is SignInState.Content -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is SignInState.Error -> {
                    Toast.makeText(
                        this,
                        "Sign In Unsuccessful ${state.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }


}