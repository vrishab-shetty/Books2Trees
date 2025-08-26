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
import com.company.books2trees.R
import com.company.books2trees.ViewBindingFragment
import com.company.books2trees.databinding.FragmentHomeBinding
import com.company.books2trees.ui.AutoFitRecyclerView
import com.company.books2trees.ui.common.AdHandler
import com.company.books2trees.ui.common.BookListAdapter
import com.company.books2trees.ui.home.adapter.HomeParentItemAdapterPreview
import com.company.books2trees.ui.home.callbacks.OnBookClicked
import com.company.books2trees.ui.home.viewState.HomeViewState
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.utils.UIHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : ViewBindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    OnBookClicked {

    private val vm: HomeViewModel by viewModel()
    private var adManager: AdHandler? = null

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

        val pageListAdapter = HomeParentItemAdapterPreview(
            mutableListOf(),
            layoutInflater,
            { text, list -> onClickListHeading(text, list) },
            this,
            vm
        )


        useBinding { binding ->
            binding.pageList.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = pageListAdapter
                setHasFixedSize(true)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.items.collectLatest { viewState ->

                    useBinding { binding ->
                        when (viewState) {
                            is HomeViewState.Loading -> {
                                binding.apply {
                                    pageList.isVisible = false
                                    homeLoading.isVisible = true
                                }
                            }

                            is HomeViewState.Content -> {
                                binding.apply {
                                    homeLoading.isVisible = false
                                    pageList.isVisible = true
                                }
                                pageListAdapter.updateList(
                                    HomePageList.getListFromMap(viewState.items), binding.pageList
                                )
                            }

                            is HomeViewState.Error -> {
                                binding.apply {

                                }
                            }
                        }
                    }


                }
            }
        }

    }


    private fun onClickListHeading(text: String, list: List<BookModel>) {
        val sheetDialog = context?.let { BottomSheetDialog(it) }

        sheetDialog?.apply {
            setContentView(R.layout.home_book_item_expanded)
            setOnDismissListener {
                UIHelper.dismissSafely(this@apply)
            }
            val headingView = this@apply.findViewById<FrameLayout>(R.id.home_expanded_drag_down)
            headingView?.setOnClickListener {
                UIHelper.dismissSafely(this@apply)
            }

            val sheetTextView = this@apply.findViewById<TextView>(R.id.home_expanded_text)
            sheetTextView?.text = text

            val recyclerView =
                this@apply.findViewById<AutoFitRecyclerView>(R.id.home_expanded_recycler)
            recyclerView?.apply {
                adapter = BookListAdapter(
                    layoutInflater,
                    this@HomeFragment,
                    null
                ).also { it.submitList(list) }
            }
            show()
        }

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

    override fun onDetach() {
        super.onDetach()
        adManager = null
    }
}
