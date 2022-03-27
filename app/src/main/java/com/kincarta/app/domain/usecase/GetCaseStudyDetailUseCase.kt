package com.kincarta.app.domain.usecase

import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.data.source.local.entity.FavoriteCaseStudyEntity
import com.kincarta.app.data.source.local.entity.SectionEntity
import com.kincarta.app.domain.repository.ICaseStudiesDetailsRepository
import javax.inject.Inject

/**
 * An interactor that calls the actual implementation of [GetCaseStudyDetailViewModel](which is injected by DI)
 * it handles the response that returns data &
 * contains a list of actions, event steps
 */
class GetCaseStudyDetailUseCase @Inject constructor(
    private val repository: ICaseStudiesDetailsRepository
) {

    suspend fun getUseCaseDetail(id: Long): Pair<CaseStudyEntity, List<SectionEntity>> {
        return repository.getCaseStudyDetail(id)
    }

    fun favoriteUseCaseDetail(caseStudyEntity: FavoriteCaseStudyEntity) {
        return repository.favUnFavCaseStudy(caseStudyEntity)
    }

    fun getAllFavoriteCaseStudies(): List<FavoriteCaseStudyEntity> {
        return repository.getAllFavoriteCaseStudies()
    }

    suspend fun getFavoriteCaseStudyByID(id: Int): FavoriteCaseStudyEntity {
        return repository.getFavoriteCaseStudyByID(id)
    }

    suspend fun loadAllCaseStudiesFromDB(): List<CaseStudyEntity> {
        return repository.loadAllCaseStudiesFromDB()
    }
}
