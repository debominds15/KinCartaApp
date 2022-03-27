package com.kincarta.app.domain.repository

import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.data.source.local.entity.SectionEntity
import com.kincarta.app.domain.model.CaseStudiesResponse
import io.reactivex.Single

/**
 * To make an interaction between [CaseStudiesRepositoryImpl] & [GetCaseStudiesUseCase]
 * */
interface ICaseStudiesRepository {
    fun getAllCaseStudies(): Single<CaseStudiesResponse>
    fun saveAllCaseStudies(
        caseStudiesData: List<CaseStudyEntity>,
        sections: List<SectionEntity>
    ): Boolean
    suspend fun loadAllCaseStudiesFromDB(): List<CaseStudyEntity>
}