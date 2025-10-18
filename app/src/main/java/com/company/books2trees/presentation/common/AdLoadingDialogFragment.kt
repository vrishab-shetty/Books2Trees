package com.company.books2trees.presentation.common

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.company.books2trees.R

class AdLoadingDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_ad_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    companion object {
        const val TAG = "AdLoadingDialog"

        fun newInstance(): AdLoadingDialogFragment {
            return AdLoadingDialogFragment()
        }
    }
}