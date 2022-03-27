package com.kincarta.app.data.repository

import com.kincarta.app.data.source.local.AppDatabase
import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.data.source.local.entity.FavoriteCaseStudyEntity
import com.kincarta.app.data.source.local.entity.SectionEntity
import com.kincarta.app.domain.repository.ICaseStudiesDetailsRepository


/**
 * This repository is responsible for
 * fetching data [case studies details] from server or db
 * */
class CaseStudiesDetailsRepositoryImpl(
    private val database: AppDatabase
) : ICaseStudiesDetailsRepository {

    override suspend fun getCaseStudyDetail(id: Long): Pair<CaseStudyEntity, List<SectionEntity>> {
        val caseStudyData = database.caseStudyDao.loadCaseStudyById(id)
        val sectionsData = database.sectionDao.loadSectionsByCaseStudyId(id)

        return Pair(caseStudyData, sectionsData)
    }

    override fun favUnFavCaseStudy(caseStudyEntity: FavoriteCaseStudyEntity) {
        database.favoritesDao.insert(caseStudyEntity)
    }

    override fun getAllFavoriteCaseStudies(): List<FavoriteCaseStudyEntity> {
        return database.favoritesDao.loadAll()
    }

    override suspend fun getFavoriteCaseStudyByID(id: Int): FavoriteCaseStudyEntity {
        return database.favoritesDao.loadFavoritesByCaseStudyId(id)
    }

    override suspend fun loadAllCaseStudiesFromDB(): List<CaseStudyEntity> {
        return database.caseStudyDao.loadAll()
    }
}