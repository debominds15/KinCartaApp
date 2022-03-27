package com.kincarta.app.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kincarta.app.data.source.local.entity.SectionEntity

/**
 * it provides access to [Section] underlying database
 * */
@Dao
interface SectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSections(sections: List<SectionEntity>)

    @Query("SELECT * FROM Section")
    fun loadAll(): List<SectionEntity>

    @Query("DELETE FROM Section")
    fun deleteAll()

    @Query("SELECT * FROM Section where caseStudyId = :caseStudyId")
    suspend fun loadSectionsByCaseStudyId(caseStudyId: Long): List<SectionEntity>
}
