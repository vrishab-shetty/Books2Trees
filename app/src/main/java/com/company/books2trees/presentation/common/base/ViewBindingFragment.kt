package com.company.books2trees.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class ViewBindingFragment<Binding : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> Binding
) : Fragment() {
    private var binding: Binding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bindingInflater(inflater, container, false)
            .apply { binding = this }
            .root
    }

    override fun onDestroyView() {
        binding = null

        super.onDestroyView()
    }

    private fun requireBinding(): Binding = binding
        ?: throw IllegalStateException("You used the binding before onCreateView() or after onDestroyView()")

    protected fun useBinding(bindingUse: (Binding) -> Unit) {
        bindingUse(requireBinding())
    }
}