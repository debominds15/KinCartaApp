package com.kincarta.app.domain.model

import com.google.gson.annotations.SerializedName

data class CaseStudiesResponse(
    @SerializedName("case_studies")
    var caseStudies: List<CaseStudies>
)