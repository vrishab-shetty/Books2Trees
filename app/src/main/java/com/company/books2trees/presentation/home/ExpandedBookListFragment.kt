package com.company.books2trees.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.os.BundleCompat
import com.company.books2trees.R
import com.company.books2trees.databinding.HomeBookItemExpandedBinding
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.common.adapter.BookListAdapter
import com.company.books2trees.presentation.common.base.ViewBindingBottomSheetDialogFragment
import com.company.books2trees.presentation.common.views.GridSpacingItemDecoration
import com.company.books2trees.presentation.home.callbacks.OnBookClicked
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ExpandedBookListFragment : ViewBindingBottomSheetDialogFragment<HomeBookItemExpandedBinding>(
    HomeBookItemExpandedBinding::inflate // Pass the inflater
), OnBookClicked {

    private val vm: HomeViewModel by sharedViewModel()

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_BOOKS = "arg_books"

        fun newInstance(title: String, books: List<BookModel>): ExpandedBookListFragment {
            return ExpandedBookListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putParcelableArrayList(ARG_BOOKS, ArrayList(books))
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(ARG_TITLE)
        val books = arguments?.let {
            BundleCompat.getParcelableArrayList(it, ARG_BOOKS, BookModel::class.java)
        }
        useBinding { binding ->
            binding.homeExpandedText.text = title
            binding.homeExpandedDragDown.setOnClickListener { dismiss() }
        }

        if (books != null) {
            setupRecyclerView(books)
        }
    }

    private fun setupRecyclerView(books: List<BookModel>) = useBinding { binding ->
        binding.homeExpandedRecycler.apply {
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_item_spacing)
            addItemDecoration(GridSpacingItemDecoration(manager.spanCount, spacingInPixels, true))

            adapter = BookListAdapter(
                layoutInflater,
                this@ExpandedBookListFragment,
                null,
                R.layout.item_book_grid
            ).also { it.submitList(books) }
        }
    }

    override fun openBook(model: BookModel) {
        vm.onBookClicked(model)
        dismiss()
    }

}