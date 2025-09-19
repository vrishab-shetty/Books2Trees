package com.company.books2trees.presentation.info

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.R
import com.company.books2trees.databinding.FragmentInfoBinding
import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.presentation.common.base.ViewBindingFragment
import com.company.books2trees.presentation.info.adapter.PdfListAdapter
import com.company.books2trees.presentation.utils.UIHelper.navigateTo
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoFragment : ViewBindingFragment<FragmentInfoBinding>(FragmentInfoBinding::inflate) {
    private lateinit var adapter: PdfListAdapter
    private val vm: InfoViewModel by viewModel()
    private val openDoc =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            it?.let {
                vm.onUriReceived(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupPdfList()
        setUpFao()
        observeViewState()
    }

    private fun observeViewState() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.pdfList.collect { viewState ->
                setupViewVisibility(viewState)

                when (viewState) {
                    is PdfListViewState.Loading -> {}

                    is PdfListViewState.Content -> useBinding { binding ->
                        val list = viewState.list

                        if (list.isEmpty()) {
                            binding.textView.text = getString(R.string.no_items)
                        }
                        adapter.submitList(viewState.list)
                    }

                    is PdfListViewState.Error -> useBinding { binding ->
                        binding.textView.text = viewState.errorMessage
                    }
                }
            }
        }
    }

    private fun setupViewVisibility(viewState: PdfListViewState) {
        useBinding { binding ->
            binding.progressBar.isVisible = viewState is PdfListViewState.Loading
            binding.textView.isVisible = viewState is PdfListViewState.Content && viewState.list.isEmpty() || viewState is PdfListViewState.Error
            binding.pdfList.isVisible = viewState is PdfListViewState.Content
        }
    }

    private fun setupPdfList() = useBinding { binding ->
            binding.pdfList.apply {
                adapter = this@InfoFragment.adapter
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val fab = binding.addPdf
                        if (dy > 0) {
                            fab.shrink()
                        } else if (dy < -5) {
                            fab.extend()
                        }
                        super.onScrolled(recyclerView, dx, dy)
                    }
                })
            }


    }

    private fun setUpFao() = useBinding { binding ->
        binding.addPdf.setOnClickListener {
            openDoc.launch(arrayOf("application/pdf"))
        }
    }

    private fun setupAdapter() {
        adapter = PdfListAdapter(
            layoutInflater,
            onItemClick = {
                openPdf(it)
            },
            onOptionsCLick = {
                deletePdf(it)
            }
        )
    }

    private fun deletePdf(model: PdfModel) {
        vm.onDeletePdf(model)
    }

    private fun openPdf(model: PdfModel) {
        activity?.navigateTo(R.id.viewPdfActivity, Bundle().apply {
            putString("pdfFileUri", model.uri.toString())
            putString("pdfName", model.name)
        })
    }

}