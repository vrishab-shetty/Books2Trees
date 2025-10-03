package com.company.books2trees.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.databinding.FragmentHomeBinding
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.domain.model.CategoryId
import com.company.books2trees.presentation.common.AdLoadingDialogFragment
import com.company.books2trees.presentation.common.base.ViewBindingFragment
import com.company.books2trees.presentation.home.adapter.HomeAdapter
import com.company.books2trees.presentation.home.adapter.HomePageList
import com.company.books2trees.presentation.home.adapter.HomePageListItem
import com.company.books2trees.presentation.home.viewState.AdUiState
import com.company.books2trees.presentation.home.viewState.HomeEvent
import com.company.books2trees.presentation.home.viewState.HomeViewState
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
        setupFragmentResultListener()

    }

    private fun setupFragmentResultListener() {
        childFragmentManager.setFragmentResultListener(
            ExpandedBookListFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val selectedBook = BundleCompat.getParcelable(
                bundle,
                ExpandedBookListFragment.BUNDLE_KEY,
                BookModel::class.java
            )

            selectedBook?.let { book ->
                vm.onEvent(HomeEvent.OpenBook(book, requireActivity()))
            }
        }
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
                    vm.onEvent(HomeEvent.AddToLibrary(book, CategoryId.PlanToRead))
                }

                override fun onBookRemove(book: BookModel) {
                    vm.onEvent(HomeEvent.RemoveFromRecents(book))
                }

                override fun onListHeadingClicked(
                    title: String,
                    books: List<BookModel>
                ) {
                    vm.onEvent(HomeEvent.ExpandList(title, books))
                    ExpandedBookListFragment.newInstance(title, books)
                        .show(childFragmentManager, "ExpandedBookListFragment")
                }

                override fun openBook(model: BookModel) {
                    vm.onEvent(HomeEvent.OpenBook(model, requireActivity()))
                }

            }
        )
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
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

        viewLifecycleOwner.lifecycleScope.launch {
            vm.adState.collect { state ->
                dismissLoadingDialog()
                when (state) {
                    is AdUiState.Idle -> {}
                    is AdUiState.Loading -> {
                        AdLoadingDialogFragment.newInstance().show(
                            childFragmentManager, AdLoadingDialogFragment.TAG
                        )
                    }

                    is AdUiState.RewardGranted -> {
                        state.url?.let {
                            openUrl(it)
                        }
                        vm.onAdFlowComplete()
                    }

                    is AdUiState.Error -> {
                        Toast.makeText(
                            context,
                            "Ad not ready. Try again later.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        vm.onAdFlowComplete()
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

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW).apply { data = url.toUri() })
    }

    private fun dismissLoadingDialog() {
        val dialog =
            childFragmentManager.findFragmentByTag(AdLoadingDialogFragment.TAG) as? DialogFragment
        dialog?.dismiss()
    }

}
