package com.kincarta.app.presentation.views

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kincarta.app.domain.model.CaseStudies

class CaseStudyViewModel(val caseStudy: CaseStudies) : ViewModel() {

    val caseStudyData = MutableLiveData<CaseStudies>()

    init {
        caseStudyData.value = caseStudy
    }
}