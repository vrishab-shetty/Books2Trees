package com.company.books2trees.ui.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog

class BasicAlertDialog(
    context: Context,
    @StringRes message: Int,
    @StringRes title: Int,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: (() -> Unit)? = null,
    @StringRes positiveButtonText: Int = android.R.string.ok,
    @StringRes negativeButtonText: Int? = null
) {


    private val builder: AlertDialog.Builder by lazy { AlertDialog.Builder(context) }
    private var dialog: AlertDialog

    init {
        builder.apply {
            setMessage(message)
            setTitle(title)
            setPositiveButton(positiveButtonText) { _, _ ->
                onPositiveButtonClick()
            }
            if (negativeButtonText != null) {
                setNegativeButton(negativeButtonText) { _, _ ->
                    onNegativeButtonClick?.invoke()
                }
            }
        }
        dialog = builder.create()
    }

    fun show() {
        dialog.show()
    }


}