package com.kincarta.app.presentation.views

import android.widget.ImageView

/**
 * To make an interaction between [CaseStudyAdapter] & [CaseStudiesFragment]
 * */
interface OnCaseStudiesAdapterListener {
    fun gotoDetailPage(imageView: ImageView, id: Long)
}