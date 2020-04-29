package com.example.diagnal.common.utils

import android.content.Context

fun getImage(mContext: Context, imageFileName: String?): Int {
    imageFileName?.substringBefore('.').let {
        return mContext.resources.getIdentifier(it, "drawable", mContext.packageName)
    }
}