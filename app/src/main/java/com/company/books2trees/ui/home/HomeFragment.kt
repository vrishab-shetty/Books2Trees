package com.company.books2trees.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.R
import com.company.books2trees.ViewBindingFragment
import com.company.books2trees.databinding.FragmentHomeBinding
import com.company.books2trees.ui.AutoFitRecyclerView
import com.company.books2trees.ui.common.AdHandler
import com.company.books2trees.ui.common.BookListAdapter
import com.company.books2trees.ui.home.adapter.HomeAdapter
import com.company.books2trees.ui.home.adapter.HomePageListItem
import com.company.books2trees.ui.home.viewState.HomeViewState
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.profile.LibraryPageItem
import com.company.books2trees.utils.UIHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : ViewBindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    HomeAdapter.Listener {

    private val vm: HomeViewModel by viewModel()
    private var adManager: AdHandler? = null

    private lateinit var homeAdapter: HomeAdapter
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is AdHandler) {
            adManager = context
        } else {
            throw RuntimeException("$context must implement AdManager")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adManager?.loadAd()

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
        adManager?.apply {
            if (isInitialized()) {
                showAd {
                    startActivity(Intent(Intent.ACTION_VIEW).apply {
                        data = model.url?.toUri()
                    })
                }
            }
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
                adapter = BookListAdapter(
                    layoutInflater,
                    this@HomeFragment,
                    null
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

    override fun onDetach() {
        super.onDetach()
        adManager = null
    }
}
