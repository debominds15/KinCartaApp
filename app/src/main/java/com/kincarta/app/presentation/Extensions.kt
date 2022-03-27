package com.kincarta.app.presentation

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.kincarta.app.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


fun ImageView.loadImage(url: String, progressBar: ProgressBar) =
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.ic_baseline_photo_default_24)
        .into(
            this,
            object : Callback {

                override fun onError(e: java.lang.Exception?) {
                    e?.printStackTrace()
                }

                override fun onSuccess() {
                    progressBar.visibility = View.GONE
                }
            }
        )
