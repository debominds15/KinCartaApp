package com.kincarta.app.domain.usecase.base

import com.kincarta.app.data.mapper.toEntity
import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.data.source.local.entity.SectionEntity
import com.kincarta.app.domain.model.CaseStudiesResponse
import com.kincarta.app.domain.repository.ICaseStudiesRepository
import io.reactivex.Single
import javax.inject.Inject


/**
 * An interactor that calls the actual implementation of [CaseStudiesViewModel](which is injected by DI)
 * it handles the response that returns data &
 * contains a list of actions, event steps
 */
class GetCaseStudiesUseCase @Inject constructor(
    private val repository: ICaseStudiesRepository
) : SingleUseCase<CaseStudiesResponse>() {


    override fun buildUseCaseSingle(): Single<CaseStudiesResponse> {
        return repository.getAllCaseStudies()
    }

    fun <T> saveCaseStudies(vararg data: T): Boolean {
        val caseStudiesDataResponse = data[0] as CaseStudiesResponse
        val caseStudyEntities = caseStudiesDataResponse.run {
            caseStudies.map {
                it.toEntity()
            }
        }

        val sectionEntities = arrayListOf<SectionEntity>()
        var secId = 1
        caseStudiesDataResponse?.let {
            it.caseStudies.forEach { caseStudy ->

                caseStudy.sections?.forEach { section ->
                    val imageUrls = arrayListOf<String>()
                    val bodyElementsStrings = arrayListOf<String>()

                    section.bodyElements?.forEach { element ->
                        if (element.isJsonPrimitive && element.asJsonPrimitive.isString) {
                            bodyElementsStrings.add(element.asString)
                        } else if (element.isJsonObject) {
                            val imageUrl = element.asJsonObject.get("image_url").asString
                            imageUrls.add(imageUrl)
                        }
                    }
                    sectionEntities.add(
                        section.toEntity(
                            secId,
                            caseStudy.id ?: 0,
                            bodyElementsStrings.joinToString("#"),
                            imageUrls.joinToString("#")
                        )
                    )
                    secId++
                }
            }
        }

        var isUpdated = false
        if (caseStudyEntities?.isNotEmpty() && sectionEntities.isNotEmpty())
            isUpdated = repository.saveAllCaseStudies(
                caseStudyEntities ?: listOf(),
                sectionEntities ?: listOf()
            )
        return isUpdated
    }
    suspend fun loadAllCaseStudiesFromDB(): List<CaseStudyEntity> {
        return repository.loadAllCaseStudiesFromDB()
    }
}