package com.kincarta.app.presentation.views

import android.widget.ImageView

/**
 * To make an interaction between [CaseStudiesActivity] & its children
 * */
interface OnCaseStudyCallback {
    fun gotoDetailPageByCaseStudyId(imageView: ImageView, id: Long)
}
