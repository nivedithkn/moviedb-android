package com.example.diagnal.feature.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.diagnal.R

/**
 * MainActivity is the Launcher Activity for this app. This Activity is launched when the
 * app is opened from the android launcher.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startFragment()
    }

    /**
     * startFragment is used to add a fragment to [R.id.containerFrameLayout]
     */
    private fun startFragment() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.containerFrameLayout, MainFragment())
            commit()
        }
    }
}
