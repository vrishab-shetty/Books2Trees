package com.company.books2trees.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.widget.SearchView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.company.books2trees.databinding.FragmentProfileBinding
import com.company.books2trees.domain.model.BookModel
import com.company.books2trees.presentation.common.base.ViewBindingFragment
import com.company.books2trees.presentation.home.callbacks.OnBookClicked
import com.company.books2trees.presentation.profile.adapter.ViewPagerAdapter
import com.company.books2trees.presentation.utils.collectLatestLifecycleFlow
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class ProfileFragment :
    ViewBindingFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate), OnBookClicked {

    private val VIEWPAGER_ITEM_KEY = "viewpager_item"
    private val vm: ProfileViewModel by viewModel()
    private var currentPosition = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerAdapter = ViewPagerAdapter(
            mutableListOf(),
            layoutInflater,
            this,
            vm
        )
        savedInstanceState?.getInt(VIEWPAGER_ITEM_KEY)?.let { currentPos ->
            currentPosition = currentPos
        }

        useBinding { binding ->
            binding.viewPager.adapter = viewPagerAdapter

            binding.sortFab.setOnClickListener {
                if (currentPosition != -1) {
                    vm.onSortClicked(currentPosition + 1)
                }
            }

            binding.mainSearch.apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (currentPosition != -1)
                            vm.onSearch(currentPosition + 1, query)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText.isNullOrEmpty())
                            vm.onSearchClosed()
                        return true
                    }

                })

            }

        }

        collectLatestLifecycleFlow(vm.pages) { viewState ->
            when (viewState) {
                is ProfileViewState.Loading -> {
                    useBinding {
                        it.viewPager.isVisible = false
                    }
                }

                is ProfileViewState.Content -> {
                    val pages = viewState.list
                    useBinding { binding ->

                        binding.apply {
                            viewPager.isVisible = true
                            (viewPager.adapter as? ViewPagerAdapter)?.updateItems(
                                pages,
                                binding.viewPager
                            )

                            if (currentPosition != viewPager.currentItem)
                                viewPager.setCurrentItem(currentPosition, false)


                            /*
                                Animation to scroll multiple items in ViewPage is so much
                                its better to hide the viewPage a bit
                            */

                            fun hideViewpager(distance: Int) {
                                if (distance < 3) return

                                val hideAnimation = AlphaAnimation(1f, 0f).apply {
                                    duration = distance * 50L
                                    fillAfter = true
                                }
                                val showAnimation = AlphaAnimation(0f, 1f).apply {
                                    duration = distance * 50L
                                    startOffset = distance * 100L
                                    fillAfter = true
                                }
                                viewPager.startAnimation(hideAnimation)
                                viewPager.startAnimation(showAnimation)
                            }

                            TabLayoutMediator(libraryTabLayout, viewPager) { tab, position ->
                                tab.text =
                                    LibraryPageItem.CategoryId.entries[position + 1].toString()

                                tab.view.setOnClickListener {
                                    val currentItem = viewPager.currentItem
                                    val distance = abs(position - currentItem)
                                    hideViewpager(distance)
                                }

                            }.attach()


                            viewPager.registerOnPageChangeCallback(object :
                                ViewPager2.OnPageChangeCallback() {
                                override fun onPageSelected(position: Int) {
                                    currentPosition = position
                                }
                            })
                        }

                    }
                }

                is ProfileViewState.Error -> {}
            }
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        useBinding {
            outState.putInt(VIEWPAGER_ITEM_KEY, currentPosition)
        }
        super.onSaveInstanceState(outState)
    }

    override fun openBook(model: BookModel) {
        vm.onBookClicked(model)
        // Open Book from URL
        model.url?.let {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = it.toUri()
            })
        }
    }

    private fun deleteBook(model: BookModel) {
        vm.onDeleteBookClicked(model.id)
    }
}