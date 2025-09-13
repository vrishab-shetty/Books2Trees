package com.company.books2trees.presentation.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.company.books2trees.R
import com.google.android.material.appbar.MaterialToolbar

object UIHelper {
    const val POPULAR_BOOKS_POSITION = 0
    const val AWARDED_BOOKS_POSITION = 1

    fun getHomeListNames() = arrayListOf(
        R.string.popular_books_caption,
        R.string.awarded_books_caption,
        R.string.recent_books_caption
    )

    fun dismissSafely(dialog: Dialog?) {
        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun Activity.navigateTo(destinationId: Int, bundle: Bundle?) {

        if (this !is FragmentActivity) return

        val fragment =
            obtainNavHostFragment(this.supportFragmentManager, R.id.nav_host, R.id.bottom_nav_graph)

        fragment.navController.apply {
            if (this.currentDestination?.id != destinationId) {
                this.navigate(destinationId, bundle)
            }
        }
    }


    private fun obtainNavHostFragment(
        fragmentManager: FragmentManager,
        fragmentId: Int,
        navGraphId: Int,
    ): NavHostFragment {
        // If the Nav Host fragment exists, return it
        val existingFragment = fragmentManager.findFragmentById(fragmentId) as NavHostFragment?
        existingFragment?.let { return it }

        // Otherwise, create it and return it.
        val navHostFragment = NavHostFragment.Companion.create(navGraphId)
        fragmentManager.beginTransaction()
            .add(fragmentId, navHostFragment)
            .commitNow()
        return navHostFragment
    }


    fun Activity.setUpToolbar(title: String) {
        this.let { activity ->
            activity.findViewById<MaterialToolbar>(R.id.settings_toolbar)?.let { toolbar ->
                toolbar.title = title
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24)
                toolbar.setNavigationOnClickListener { this.finish() }
            }
        }
    }

    fun Fragment.setUpToolbar(@StringRes title: Int) {
        this.activity?.let { activity ->
            activity.findViewById<MaterialToolbar>(R.id.settings_toolbar)?.let { toolbar ->
                toolbar.setTitle(title)
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24)
                toolbar.setNavigationOnClickListener {
                    val fragment = activity.supportFragmentManager.findFragmentById(R.id.nav_host)
                    if (fragment is NavHostFragment) {
                        fragment.navController.navigateUp()
                    }
                }

            }

        }
    }

    fun getScreenWidth(activity: Activity?): Int {
        return if (activity == null) 0 else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    fun Activity.hideKeyboard() {
        this.window.decorView.clearFocus()
        val view = this.findViewById<View>(android.R.id.content).rootView
        hideKeyboard(view)
    }

    fun Context.hideKeyboard(view: View) {
        val manager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.hideKeyboard() {
        this.activity?.window?.decorView?.clearFocus()
        view?.let { activity?.hideKeyboard(it) }
    }

    @SuppressLint("RestrictedApi")
    fun View.popupMenuNoIcons(
        items: List<Pair<Int, Int>>,
        onMenuItemClick: MenuItem.() -> Unit,
    ): PopupMenu {
        val ctw = ContextThemeWrapper(context, R.style.PopupMenu)
        val popup =
            PopupMenu(ctw, this, Gravity.NO_GRAVITY, android.R.attr.actionOverflowMenuStyle, 0)

        items.forEach { (id, stringRes) ->
            popup.menu.add(0, id, 0, stringRes)
        }

        (popup.menu as? MenuBuilder)?.setOptionalIconsVisible(true)

        popup.setOnMenuItemClickListener {
            it.onMenuItemClick()
            true
        }

        popup.show()
        return popup
    }
}