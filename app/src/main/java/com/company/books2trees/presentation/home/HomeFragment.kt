package com.company.books2trees.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.R
import com.company.books2trees.data.local.mapper.toBookModel
import com.company.books2trees.databinding.FragmentHomeBinding
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.common.AppAdManager
import com.company.books2trees.presentation.common.adapter.BookListAdapter
import com.company.books2trees.presentation.common.base.ViewBindingFragment
import com.company.books2trees.presentation.common.views.AutoFitRecyclerView
import com.company.books2trees.presentation.common.views.GridSpacingItemDecoration
import com.company.books2trees.presentation.home.adapter.HomeAdapter
import com.company.books2trees.presentation.home.adapter.HomePageList
import com.company.books2trees.presentation.home.adapter.HomePageListItem
import com.company.books2trees.presentation.home.viewState.HomeViewState
import com.company.books2trees.presentation.profile.LibraryPageItem
import com.company.books2trees.presentation.utils.UIHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : ViewBindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    HomeAdapter.Listener {

    private val vm: HomeViewModel by viewModel()

    private lateinit var homeAdapter: HomeAdapter
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeAdapter = HomeAdapter(layoutInflater, this, viewPool)

        useBinding { binding ->
            binding.pageList.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = homeAdapter
                setHasFixedSize(true)
            }
        }

        observeViewModel()

    }

    private fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.items.collectLatest { viewState ->
                    useBinding { binding ->
                        binding.homeLoading.isVisible = viewState is HomeViewState.Loading
                        binding.pageList.isVisible = viewState is HomeViewState.Content
                    }
                    if (viewState is HomeViewState.Content) {
                        updateAdapterList(viewState)
                    }
                }
            }
        }
    }

    private fun updateAdapterList(content: HomeViewState.Content) {
        val items = mutableListOf<HomePageListItem>()

        content.recentItems.map { it.toBookModel() }.takeIf { it.isNotEmpty() }?.let {
            items.add(HomePageListItem.RecentBooks(it))
        }

        val homePageLists = HomePageList.getListFromMap(content.items)
        items.addAll(homePageLists.map { HomePageListItem.BookList(it) })

        homeAdapter.submitList(items)
    }

    // UI Action triggers the call
    override fun openBook(model: BookModel) {
        vm.onBookClicked(model)

        // Open Book from URL
        if (AppAdManager.isAdReady()) {
            AppAdManager.showAd(requireActivity()) {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = model.url?.toUri()
                })
            }
        } else {
            Toast.makeText(context, "Ad not ready opening content.", Toast.LENGTH_LONG).show()
        }


    }

    override fun onListHeadingClicked(
        title: String,
        books: List<BookModel>
    ) {
        val sheetDialog = context?.let { BottomSheetDialog(it) }

        sheetDialog?.apply {
            setContentView(R.layout.home_book_item_expanded)
            setOnDismissListener { UIHelper.dismissSafely(this@apply) }

            findViewById<FrameLayout>(R.id.home_expanded_drag_down)?.setOnClickListener {
                UIHelper.dismissSafely(this@apply)
            }

            findViewById<TextView>(R.id.home_expanded_text)?.text = title

            val recyclerView = findViewById<AutoFitRecyclerView>(R.id.home_expanded_recycler)
            recyclerView?.apply {

                val spacingInPixels =
                    resources.getDimensionPixelSize(R.dimen.grid_item_spacing) // e.g., 8dp
                addItemDecoration(
                    GridSpacingItemDecoration(
                        manager.spanCount,
                        spacingInPixels,
                        true
                    )
                )

                adapter = BookListAdapter(
                    layoutInflater,
                    this@HomeFragment,
                    null,
                    R.layout.item_book_grid
                ).also { it.submitList(books) }
            }
            show()
        }
    }

    override fun onBookAddToFavorites(book: BookModel) {
        vm.insertToLibrary(book, LibraryPageItem.CategoryId.PlanToRead)
    }

    override fun onBookRemove(book: BookModel) {
        vm.onRemoveClicked(book)
    }
}
