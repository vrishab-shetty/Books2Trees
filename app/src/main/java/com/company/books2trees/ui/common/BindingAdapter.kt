package com.company.books2trees.ui.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.company.books2trees.R

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    url?.let {
        this.load(it) {
            placeholder(R.drawable.image_placeholder_24)
        }
    }
}