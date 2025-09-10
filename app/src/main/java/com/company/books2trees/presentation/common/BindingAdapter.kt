package com.company.books2trees.presentation.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.company.books2trees.R

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    if (!url.isNullOrBlank()) {
        this.load(url) {
            placeholder(R.drawable.image_placeholder_24)
            error(R.drawable.image_placeholder_24)
        }
    } else {
        this.setImageResource(R.drawable.image_placeholder_24)
    }
}