package com.company.books2trees.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.books2trees.R
import com.company.books2trees.databinding.FragmentSearchBinding
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.common.base.ViewBindingFragment
import com.company.books2trees.presentation.home.callbacks.OnBookClicked
import com.company.books2trees.presentation.search.adapter.SearchResultBookAdapter
import com.company.books2trees.presentation.utils.UIHelper
import com.company.books2trees.presentation.utils.UIHelper.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class SearchFragment : ViewBindingFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate), OnBookClicked {

    private val vm: SearchViewModel by viewModels()
    private lateinit var searchResultBookAdapter: SearchResultBookAdapter
    private var filterListView: ListView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchResultBookAdapter =
            SearchResultBookAdapter(layoutInflater, this@SearchFragment)

        useBinding { binding ->
            binding.searchResultList.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = searchResultBookAdapter
            }

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

            binding.searchFilter.setOnClickListener {
                onClickFilter(requireContext())
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.result.collect { viewState ->
                        when (viewState) {
                            is ResultViewState.Loading ->
                                useBinding { binding ->
                                    binding.searchLoadingBar.visibility = View.VISIBLE
                                }

                            is ResultViewState.Content -> {
                                useBinding { binding ->
                                    binding.searchLoadingBar.visibility = View.GONE
                                }
                                searchResultBookAdapter.submitList(viewState.list)
                            }

                            is ResultViewState.Error -> {
                                useBinding { binding ->
                                    binding.searchLoadingBar.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
                launch {
                    vm.selectedFilter.collect { choice ->
                        filterListView?.let {
                            setSelectedItem(
                                it, choice
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onClickFilter(context: Context) {

        val sheetDialog = BottomSheetDialog(context)
        sheetDialog.apply {
            setContentView(R.layout.select_list_page)
            setOnDismissListener { UIHelper.dismissSafely(this@apply) }
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            val listView = this.findViewById<ListView>(R.id.listview)
            listView?.apply {
                this@SearchFragment.filterListView = listView
                val adapter =
                    ArrayAdapter(
                        context,
                        R.layout.filter_single_choice,
                        vm.genreList
                    ) as ListAdapter
                setAdapter(adapter)
                choiceMode = ListView.CHOICE_MODE_SINGLE
                setSelectedItem(this)
                setOnItemClickListener { _, _, position, _ ->
                    setItemChecked(position, true)
                    vm.onFilterItemClicked(position)
                }
            }

            val applyBtn = this.findViewById<MaterialButton>(R.id.apply_btn)
            applyBtn?.setOnClickListener {
                vm.applyFilter()
                UIHelper.dismissSafely(this@apply)
            }

            val cancelBtn = this.findViewById<MaterialButton>(R.id.cancel_btn)
            cancelBtn?.setOnClickListener {
                UIHelper.dismissSafely(this@apply)
            }
            show()
        }
    }

    private fun setSelectedItem(listView: ListView, choice: String? = null) {
        val adapter = listView.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i) == choice) {
                listView.setItemChecked(i, true)
            } else {
                listView.setItemChecked(i, false)
            }
        }
    }

    // UI Action triggers the call
    override fun openBook(model: BookModel) {
        // Open Book from URL
        // ...
        vm.onBookClicked(model)

        model.url?.let {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = it.toUri()
            })
        }
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }


}