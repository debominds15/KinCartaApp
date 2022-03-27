package com.kincarta.app.data.mapper

import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.domain.model.CaseStudies

fun CaseStudyEntity.toOriginal() = CaseStudies(
    id = id,
    client = client,
    teaser = teaser,
    vertical = vertical,
    isEnterprise = isEnterprise,
    title = title,
    heroImage = heroImage,
    sections = listOf()
)
