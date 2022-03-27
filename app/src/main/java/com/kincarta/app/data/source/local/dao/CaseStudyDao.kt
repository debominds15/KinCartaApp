package com.kincarta.app.data.source.local.dao

import androidx.room.*
import com.kincarta.app.data.source.local.entity.CaseStudyEntity

/**
 * it provides access to [Case Study] underlying database
 * */
@Dao
interface CaseStudyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCaseStudies(sections: List<CaseStudyEntity>)

    @Query("SELECT * FROM CaseStudy")
    fun loadAll(): MutableList<CaseStudyEntity>

    @Delete
    fun delete(caseStudy: CaseStudyEntity)

    @Query("DELETE FROM CaseStudy")
    fun deleteAll()

    @Query("SELECT * FROM CaseStudy where id = :caseStudyId")
    suspend fun loadCaseStudyById(caseStudyId: Long): CaseStudyEntity
}
