package com.company.books2trees.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.company.books2trees.R
import com.company.books2trees.databinding.SelectListPageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: SelectListPageBinding? = null
    private val binding get() = _binding!!

    private val vm: SearchViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectListPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListView()
        setupApplyButton()
        setupCancelButton()
        observeSelectedFilter()
    }

    private fun setupCancelButton() {
        binding.cancelBtn.setOnClickListener {
            dismiss() // Close the bottom sheet
        }
    }

    private fun setupApplyButton() {
        binding.applyBtn.setOnClickListener {
            vm.applyFilter()
            dismiss() // Close the bottom sheet
        }
    }

    private fun setupListView() {
        val genreAdapter = ArrayAdapter(
            requireContext(),
            R.layout.filter_single_choice, // Use Android's built-in layout
            vm.genreList
        )

        binding.listview.apply {
            adapter = genreAdapter
            choiceMode = ListView.CHOICE_MODE_SINGLE
            setOnItemClickListener { _, _, position, _ ->
                vm.onFilterItemClicked(position)
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
                        binding.listview.setItemChecked(position, true)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}