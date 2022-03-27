package com.kincarta.app.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite")
data class FavoriteCaseStudyEntity(
    @PrimaryKey
    var id: Int,
    var isFab: Int = 0
)