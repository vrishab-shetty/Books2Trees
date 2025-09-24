package com.company.books2trees.presentation.search

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.company.books2trees.R
import com.company.books2trees.databinding.SelectListPageBinding
import com.company.books2trees.presentation.common.base.ViewBindingBottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterBottomSheetDialogFragment : ViewBindingBottomSheetDialogFragment<SelectListPageBinding>(
    SelectListPageBinding::inflate
) {

    private val vm: SearchViewModel by sharedViewModel()
    private lateinit var genreAdapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListView()
        setupApplyButton()
        setupCancelButton()
        observeGenreList()
        observeSelectedFilter()
    }

    private fun observeGenreList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.genreList.collect { genres ->
                    genreAdapter.apply {
                        clear()
                        addAll(genres)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun setupCancelButton() = useBinding { binding ->
        binding.cancelBtn.setOnClickListener {
            dismiss() // Close the bottom sheet
        }
    }

    private fun setupApplyButton() = useBinding { binding ->
        binding.applyBtn.setOnClickListener {
            vm.applyFilter()
            dismiss() // Close the bottom sheet
        }
    }

    private fun setupListView() {
        genreAdapter = ArrayAdapter(
            requireContext(), R.layout.filter_single_choice, mutableListOf()
        )

        useBinding { binding ->
            binding.listview.apply {
                adapter = genreAdapter
                choiceMode = ListView.CHOICE_MODE_SINGLE
                setOnItemClickListener { _, _, position, _ ->
                    val selectedGenre = genreAdapter.getItem(position)
                    vm.onFilterItemClicked(selectedGenre!!)
                }
            }
        }

    }

    private fun observeSelectedFilter() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.selectedFilter.collect { currentFilter ->
                    // Find the position of the current filter in the list
                    val position = genreAdapter.getPosition(currentFilter)
                    if (position != -1) {
                        // Update the ListView's selection
                        useBinding { binding -> binding.listview.setItemChecked(position, true) }
                    }
                }
            }
        }
    }
}