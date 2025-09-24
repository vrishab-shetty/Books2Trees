package com.company.books2trees.presentation.info.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.databinding.ItemViewPdfBinding
import com.company.books2trees.presentation.info.PdfViewerUiState
import com.company.books2trees.presentation.info.PdfViewerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * An adapter for the ViewPager2 that displays PDF pages.
 * It is a "dumb" component that gets its data from the ViewModel.
 */
class ViewPdfAdapter(
    private val viewModel: PdfViewerViewModel,
    private val adapterScope: CoroutineScope
) : RecyclerView.Adapter<ViewPdfAdapter.ViewPdfHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPdfHolder {
        val binding = ItemViewPdfBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewPdfHolder(binding)
    }

    // The number of items is now driven by the pageCount in the ViewModel's state.
    // The Activity will notify the adapter when the pageCount is available.
    override fun getItemCount(): Int {
        val state = viewModel.uiState.value
        return if (state is PdfViewerUiState.Content) {
            state.pageCount
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: ViewPdfHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewPdfHolder(private val binding: ItemViewPdfBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        private var renderJob: Job? = null

        fun bind(position: Int) {
            // Cancel any previous render job for this holder, as it's being recycled.
            renderJob?.cancel()

            // The key change is here: Use post() to wait for the view to be laid out.
            binding.root.post {
                // By the time this block runs, the view's width is known.
                val renderWidth = binding.root.width

                // Only proceed if the width is valid and the view is still attached.
                if (renderWidth > 0) {
                    // Launch the coroutine to load the bitmap *inside* the post block.
                    renderJob = adapterScope.launch {
                        val bitmap = viewModel.getPageBitmap(position, renderWidth)

                        // Ensure the coroutine is still active before setting the bitmap.
                        if (isActive) {
                            binding.pdfPage.setImageBitmap(bitmap)
                        }
                    }
                }
            }
        }
    }
}

