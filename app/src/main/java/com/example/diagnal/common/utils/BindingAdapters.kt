package com.example.diagnal.common.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.diagnal.R

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
        .load(getImage(imageView.context, url))
        .fitCenter()
        .placeholder(R.drawable.placeholder_for_missing_posters)
        .into(imageView)
}
