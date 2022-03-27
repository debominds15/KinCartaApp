package com.kincarta.app.presentation.views.CaseStudies

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.kincarta.app.presentation.views.OnCaseStudyCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity(), OnCaseStudyCallback {
    override fun gotoDetailPageByCaseStudyId(imageView: ImageView, id: Long) {
        TODO("Not yet implemented")
    }
}