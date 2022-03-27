package com.kincarta.app.feature_note.data.repository

import com.kincarta.app.data.mapper.toEntity
import com.kincarta.app.data.mapper.toOriginal
import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.data.source.local.entity.SectionEntity
import com.kincarta.app.domain.model.CaseStudies
import com.kincarta.app.domain.model.CaseStudiesResponse
import com.kincarta.app.domain.model.Sections
import com.kincarta.app.domain.repository.ICaseStudiesRepository
import io.reactivex.Single
import org.junit.Test

class FakeCaseStudiesRepository : ICaseStudiesRepository {

    lateinit var caseStudiesData: MutableList<CaseStudies>

    private var caseStudies :CaseStudiesResponse = CaseStudiesResponse(listOf())

    init {
        //setUpMockData()
        //caseStudies  = CaseStudiesResponse(caseStudiesData)
    }

    fun setUpMockData(){
        caseStudiesData = mutableListOf<CaseStudies>()
        ('a'..'z').forEachIndexed { index, c ->
            caseStudiesData.add(
                CaseStudies(
                    id = index,
                    title = c.toString()
                )
            )
        }
    }

    override fun getAllCaseStudies(): Single<CaseStudiesResponse> {
       return Single.just(caseStudies)
    }

    override fun saveAllCaseStudies(
        caseStudiesData: List<CaseStudyEntity>,
        sections: List<SectionEntity>
    ): Boolean {
        this.caseStudies.caseStudies = caseStudiesData.map {
            it.toOriginal()
        }.toMutableList()
        return true
    }

    override suspend fun loadAllCaseStudiesFromDB(): List<CaseStudyEntity> {
        return caseStudies.caseStudies.map { it.toEntity() }
    }
}