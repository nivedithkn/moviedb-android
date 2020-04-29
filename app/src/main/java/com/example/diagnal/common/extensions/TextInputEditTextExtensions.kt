package com.example.diagnal.common.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_main.*
/**
 * This file contains Kotlin Extension functions for [EditText]
 */
fun EditText.showKeyboard(){
    val imm =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.hideKeyboard(){
    val imm =
        this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}