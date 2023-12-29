package com.company.books2trees.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.books2trees.R
import com.company.books2trees.ViewBindingFragment
import com.company.books2trees.databinding.FragmentSearchBinding
import com.company.books2trees.ui.common.BookListAdapter
import com.company.books2trees.ui.common.SearchResultViewType
import com.company.books2trees.ui.home.callbacks.OnBookClicked
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.utils.UIHelper
import com.company.books2trees.utils.UIHelper.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

class SearchFragment : ViewBindingFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate), OnBookClicked {

    private val vm: SearchViewModel by viewModels()
    private lateinit var searchResultAdapter: BookListAdapter
    private var filterListView: ListView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchResultAdapter =
            BookListAdapter(SearchResultViewType, layoutInflater, this, null)

        useBinding { binding ->
            binding.searchResultList.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = searchResultAdapter
            }

            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        vm.search(query)
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


        vm.result.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ResultViewState.Loading ->
                    useBinding { binding ->
                        binding.searchLoadingBar.visibility = View.VISIBLE
                    }

                is ResultViewState.Content -> {
                    useBinding { binding ->
                        binding.searchLoadingBar.visibility = View.GONE
                    }
                    searchResultAdapter.submitList(viewState.list)
                }

                is ResultViewState.Error -> {
                    useBinding { binding ->
                        binding.searchLoadingBar.visibility = View.GONE
                    }
                    Log.e(
                        "Search Fragment",
                        "Exception Search Result",
                        viewState.throwable
                    )
                }
            }
        }

        vm.selectedFilter.observe(viewLifecycleOwner) { choice ->
            filterListView?.let {
                setSelectedItem(
                    it, choice
                )
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
                    ) as android.widget.ListAdapter
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

        vm.onFilterClicked()
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
        Log.i("TAG", "openBook: ")
        vm.onBookClicked(model)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }


}