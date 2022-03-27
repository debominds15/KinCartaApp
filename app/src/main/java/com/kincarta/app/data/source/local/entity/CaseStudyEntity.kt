package com.kincarta.app.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CaseStudy")
data class CaseStudyEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var client: String? = null,
    var teaser: String? = null,
    var vertical: String? = null,
    var isEnterprise: Boolean? = null,
    var title: String? = null,
    var heroImage: String? = null,
    var isFab: Int = 0
)