package com.kincarta.app.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Section")
data class SectionEntity(
    @PrimaryKey
    var id: Int,
    var caseStudyId: Int,
    var title: String? = "",
    var bodyElements: String = "",
    var bodyImageUrls: String = ""
)
