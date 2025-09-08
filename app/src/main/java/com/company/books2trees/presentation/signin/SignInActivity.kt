package com.company.books2trees.presentation.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.company.books2trees.MainActivity
import com.company.books2trees.databinding.ActivitySignInBinding
import com.company.books2trees.presentation.utils.collectLatestLifecycleFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity() {
    private val vm: SignInViewModel by viewModel()
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInBtn.setOnClickListener {
            vm.signIn()
        }

        collectLatestLifecycleFlow(vm.state) { state ->
            when (state) {
                is SignInState.Pending -> {
                    // You could show a loading spinner here
                }

                is SignInState.Content -> {
                    // On successful sign-in, navigate to the main activity.
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
}