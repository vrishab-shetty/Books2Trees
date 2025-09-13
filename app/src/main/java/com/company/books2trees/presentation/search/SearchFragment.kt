package com.company.books2trees.presentation.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.books2trees.databinding.FragmentSearchBinding
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.common.base.ViewBindingFragment
import com.company.books2trees.presentation.home.callbacks.OnBookClicked
import com.company.books2trees.presentation.search.adapter.SearchResultBookAdapter
import com.company.books2trees.presentation.utils.UIHelper.hideKeyboard
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : ViewBindingFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate), OnBookClicked {

    private val vm: SearchViewModel by viewModel()
    private lateinit var searchResultBookAdapter: SearchResultBookAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        setupFilterButton()
        collectState()
    }

    private fun setupRecyclerView() {
        searchResultBookAdapter =
            SearchResultBookAdapter(layoutInflater, this@SearchFragment)
        useBinding { binding ->
            binding.searchResultList.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = searchResultBookAdapter
            }
        }
    }

    private fun setupSearchView() = useBinding { binding ->
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    vm.onQueryChanged(query)
                }
                hideKeyboard()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupFilterButton() = useBinding { binding ->
        binding.searchFilter.setOnClickListener {
            FilterBottomSheetDialogFragment().show(childFragmentManager, "FilterBottomSheet")
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Collect UI state (loading, content, error)
                launch {
                    vm.result.collect { viewState ->
                        handleViewState(viewState)
                    }
                }
                launch {
                    vm.navigationEvent.collect { url ->
                        navigateToBook(url)
                    }
                }
            }
        }
    }

    private fun handleViewState(viewState: ResultViewState) = useBinding { binding ->
        binding.searchLoadingBar.visibility =
            if (viewState is ResultViewState.Loading) View.VISIBLE else View.GONE

        when (viewState) {
            is ResultViewState.Loading -> binding.searchLoadingBar.visibility = View.VISIBLE

            is ResultViewState.Content -> {
                binding.searchLoadingBar.visibility = View.GONE
                searchResultBookAdapter.submitList(viewState.list)
            }

            is ResultViewState.Error -> binding.searchLoadingBar.visibility = View.GONE
        }
    }

    private fun navigateToBook(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply { data = url.toUri() }
        startActivity(intent)
    }

    override fun openBook(model: BookModel) {
        vm.onBookClicked(model)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }


}