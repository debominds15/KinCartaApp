package com.kincarta.app.data.mapper

import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.data.source.local.entity.SectionEntity
import com.kincarta.app.domain.model.CaseStudies
import com.kincarta.app.domain.model.Sections

fun CaseStudies.toEntity() = CaseStudyEntity(
    id = id ?: 0,
    client = client,
    teaser = teaser,
    vertical = vertical,
    isEnterprise = isEnterprise,
    title = title,
    heroImage = heroImage,
)

fun Sections.toEntity(id: Int, caseStudyID: Int, bodyElements: String, imageUrls: String) =
    SectionEntity(
        id = id,
        caseStudyId = caseStudyID,
        title = title,
        bodyElements = bodyElements,
        bodyImageUrls = imageUrls
    )


