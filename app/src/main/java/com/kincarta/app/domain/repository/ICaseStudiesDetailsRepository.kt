package com.kincarta.app.domain.repository

import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.data.source.local.entity.FavoriteCaseStudyEntity
import com.kincarta.app.data.source.local.entity.SectionEntity

/**
 * To make an interaction between [CaseStudiesDetailsRepositoryImpl] & [GetCaseStudiesDetailUseCase]
 * */
interface ICaseStudiesDetailsRepository {
    suspend fun getCaseStudyDetail(id: Long): Pair<CaseStudyEntity, List<SectionEntity>>
    fun favUnFavCaseStudy(caseStudyEntity: FavoriteCaseStudyEntity)
    fun getAllFavoriteCaseStudies(): List<FavoriteCaseStudyEntity>
    suspend fun getFavoriteCaseStudyByID(id: Int): FavoriteCaseStudyEntity
    suspend fun loadAllCaseStudiesFromDB(): List<CaseStudyEntity>
}