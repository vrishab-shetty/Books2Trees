package com.company.books2trees.presentation.info

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.company.books2trees.databinding.ActivityPdfViewBinding
import com.company.books2trees.presentation.info.adapter.ViewPdfAdapter
import com.company.books2trees.presentation.utils.UIHelper.setUpToolbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ViewPdfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewBinding
    private val args: InfoFragmentArgs by navArgs()

    // Inject the ViewModel using Koin's delegate.
    // Use parametersOf to pass the runtime pdfUri from navArgs to the ViewModel's constructor.
    private val vm: PdfViewerViewModel by viewModel {
        parametersOf(args.pdfFileUri)
    }

    private lateinit var viewPdfAdapter: ViewPdfAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Assumes 'args.pdfName' is the string key for the PDF's name.
        setUpToolbar(args.pdfName)
        setupViewPager()
        observeUiState()
    }


    private fun setupViewPager() {
        viewPdfAdapter = ViewPdfAdapter(vm, lifecycleScope)
        binding.viewPdf.adapter = viewPdfAdapter
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                vm.uiState.collect { state ->
                    handleState(state)
                }
            }
        }
    }

    private fun handleState(state: PdfViewerUiState) {
        when (state) {
            is PdfViewerUiState.Loading -> {
            }

            is PdfViewerUiState.Content -> {
                // Notify the adapter that the item count is now available.
                viewPdfAdapter.notifyDataSetChanged()
            }

            is PdfViewerUiState.Error -> {
                binding.errorText.text = state.message
            }
        }
        binding.viewPdf.isVisible = state is PdfViewerUiState.Content
        binding.progressBar.isVisible = state is PdfViewerUiState.Loading
        binding.errorText.isVisible = state is PdfViewerUiState.Error
    }
}

