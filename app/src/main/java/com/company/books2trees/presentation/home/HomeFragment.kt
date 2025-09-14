package com.company.books2trees.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.databinding.FragmentHomeBinding
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.common.AppAdManager
import com.company.books2trees.presentation.common.base.ViewBindingFragment
import com.company.books2trees.presentation.home.adapter.HomeAdapter
import com.company.books2trees.presentation.home.adapter.HomePageList
import com.company.books2trees.presentation.home.adapter.HomePageListItem
import com.company.books2trees.presentation.home.viewState.HomeViewState
import com.company.books2trees.presentation.profile.LibraryPageItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : ViewBindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val vm: HomeViewModel by viewModel()

    private lateinit var homeAdapter: HomeAdapter
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPageList()
        observeViewModel()

    }

    private fun setupPageList() {
        createHomeAdapter()
        useBinding { binding ->
            binding.pageList.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = homeAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun createHomeAdapter() {
        homeAdapter = HomeAdapter(
            layoutInflater, viewPool = viewPool,
            listener = object : HomeAdapter.Listener {
                override fun onBookAddToFavorites(book: BookModel) {
                    vm.insertToLibrary(book, LibraryPageItem.CategoryId.PlanToRead)
                }

                override fun onBookRemove(book: BookModel) {
                    vm.onRemoveClicked(book)
                }

                override fun onListHeadingClicked(
                    title: String,
                    books: List<BookModel>
                ) {
                    ExpandedBookListFragment.newInstance(title, books)
                        .show(childFragmentManager, "ExpandedBookListFragment")
                }

                override fun openBook(model: BookModel) {
                    this@HomeFragment.openBook(model)
                }
            }
        )
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

        content.recentItems.takeIf { it.isNotEmpty() }?.let {
            items.add(HomePageListItem.RecentBooks(it))
        }

        val homePageLists = HomePageList.getListFromMap(content.items)
        items.addAll(homePageLists.map { HomePageListItem.BookList(it) })

        homeAdapter.submitList(items)
    }

    fun openBook(model: BookModel) {
        vm.onBookClicked(model)

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

}
