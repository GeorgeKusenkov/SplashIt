package com.example.splashit.presentation

import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class Loader {
    fun showProgress(progress: ProgressBar, recyclerView: RecyclerView) {
        progress.isVisible = true
        recyclerView.isGone = true
    }

    fun hideProgress(progress: ProgressBar, recyclerView: RecyclerView) {
        progress.isVisible = false
        recyclerView.isGone = false
    }

    fun showMessage(message: TextView, recyclerView: RecyclerView) {
        message.isGone = false
        recyclerView.isGone = true
    }

    fun hideMessage(message: TextView, recyclerView: RecyclerView) {
        message.isGone = true
        recyclerView.isGone = false
    }
}