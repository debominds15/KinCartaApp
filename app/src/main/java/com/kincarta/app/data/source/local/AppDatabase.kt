package com.kincarta.app.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kincarta.app.data.source.local.dao.CaseStudyDao
import com.kincarta.app.data.source.local.dao.FavoriteDao
import com.kincarta.app.data.source.local.dao.SectionDao
import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.data.source.local.entity.FavoriteCaseStudyEntity
import com.kincarta.app.data.source.local.entity.SectionEntity

/**
 * To manage data items that can be accessed, updated
 * & maintain relationships between them
 *
 * @Created by Debojyoti
 */
@Database(
    entities = [CaseStudyEntity::class, SectionEntity::class, FavoriteCaseStudyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val caseStudyDao: CaseStudyDao
    abstract val sectionDao: SectionDao
    abstract val favoritesDao: FavoriteDao

    companion object {
        const val DB_NAME = "CaseStudySectionDatabase.db"
    }
}
