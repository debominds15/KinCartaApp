package com.kincarta.app.data.repository

import android.database.sqlite.SQLiteException
import com.kincarta.app.data.source.local.AppDatabase
import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.data.source.local.entity.SectionEntity
import com.kincarta.app.domain.model.CaseStudiesResponse
import com.kincarta.app.domain.repository.ICaseStudiesRepository
import com.kincarta.app.source.remote.RetrofitService
import io.reactivex.Single


/**
 * This repository is responsible for
 * fetching data [case studies] from server or db
 * */
class CaseStudiesRepositoryImpl(
    private val database: AppDatabase,
    private val retrofitService: RetrofitService
) : ICaseStudiesRepository {
    override fun getAllCaseStudies(): Single<CaseStudiesResponse> {
        return retrofitService.getCaseStudies()
    }

    override fun saveAllCaseStudies(
        caseStudiesData: List<CaseStudyEntity>,
        sections: List<SectionEntity>
    ): Boolean {
        database.caseStudyDao.deleteAll()
        database.sectionDao.deleteAll()

        try {
            database.caseStudyDao.insertAllCaseStudies(caseStudiesData)
            database.sectionDao.insertAllSections(sections)
        } catch (e: SQLiteException) {
            return false
        }

        return true
    }

    override suspend fun loadAllCaseStudiesFromDB(): List<CaseStudyEntity> {
        return database.caseStudyDao.loadAll()
    }
}