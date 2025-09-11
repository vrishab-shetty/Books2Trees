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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListView()
        setupApplyButton()
        setupCancelButton()
        observeSelectedFilter()
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

    private fun setupListView() = {
        val genreAdapter = ArrayAdapter(
            requireContext(),
            R.layout.filter_single_choice, // Use Android's built-in layout
            vm.genreList
        )

        useBinding { binding ->
            binding.listview.apply {
                adapter = genreAdapter
                choiceMode = ListView.CHOICE_MODE_SINGLE
                setOnItemClickListener { _, _, position, _ ->
                    vm.onFilterItemClicked(position)
                }
            }
        }

    }

    private fun observeSelectedFilter() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.selectedFilter.collect { currentFilter ->
                    // Find the position of the current filter in the list
                    val position = vm.genreList.indexOf(currentFilter)
                    if (position != -1) {
                        // Update the ListView's selection
                        useBinding { binding -> binding.listview.setItemChecked(position, true) }
                    }
                }
            }
        }
    }
}