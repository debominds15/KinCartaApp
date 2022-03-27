package com.kincarta.app.presentation.views

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kincarta.app.data.mapper.toOriginal
import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.data.source.local.entity.FavoriteCaseStudyEntity
import com.kincarta.app.data.source.local.entity.SectionEntity
import com.kincarta.app.domain.model.CaseStudies
import com.kincarta.app.domain.model.SortTypeEnum
import com.kincarta.app.domain.usecase.GetCaseStudyDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

/**
 * A helper class for the UI controller that is responsible for
 * preparing data for [CaseStudiesDetailFragment]
 *
 * @author Debojyoti
 * */
@HiltViewModel
class CaseStudyDetailViewModel @Inject constructor(
    private val caseStudiesDetailsUseCase: GetCaseStudyDetailUseCase
) : ViewModel() {

    val caseStudyDetailData = MutableLiveData<Pair<CaseStudyEntity, List<SectionEntity>>>()
    val favCaseStudyData = MutableLiveData<List<CaseStudies>>()
    val filterByCategories = MutableLiveData<List<String>>()
    lateinit var categoryFlags: BooleanArray
    val filterCaseStudyData = MutableLiveData<Pair<SortTypeEnum, List<String>>>()
    lateinit var allCaseStudies: List<CaseStudies>

    fun getCaseStudyDetail(id: Long) {
        CoroutineScope(Dispatchers.IO).async {
            val caseStudyData =
                withContext(Dispatchers.Default) {
                    caseStudiesDetailsUseCase.getUseCaseDetail(id)
                }
            val favoriteCaseStudy: FavoriteCaseStudyEntity =
                withContext(Dispatchers.Default) {
                    caseStudiesDetailsUseCase.getFavoriteCaseStudyByID(
                        caseStudyData.first.id
                    )
                }

            caseStudyData.first.isFab = favoriteCaseStudy?.isFab ?: 0
            caseStudyDetailData.postValue(caseStudyData)
        }
    }


    fun markCaseStudyDetailFav(caseStudyEntity: FavoriteCaseStudyEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            caseStudiesDetailsUseCase.favoriteUseCaseDetail(caseStudyEntity)
        }
    }

    fun getAllFavoriteCaseStudies() {
        CoroutineScope(Dispatchers.IO).launch {
            val favCases = caseStudiesDetailsUseCase.getAllFavoriteCaseStudies()
            val allCaseStudies = caseStudiesDetailsUseCase.loadAllCaseStudiesFromDB()
            val filteredCaseStudies = arrayListOf<CaseStudies>()

            favCases.forEach { favCase ->
                val case = allCaseStudies.find { caseStudy ->
                    favCase.id == caseStudy.id && favCase.isFab == 1
                }
                case?.let {
                    filteredCaseStudies.add(it.toOriginal())
                }
            }
            favCaseStudyData.postValue(filteredCaseStudies)
            this@CaseStudyDetailViewModel.allCaseStudies = filteredCaseStudies
            setUpFilterByCategories(filteredCaseStudies)
        }
    }

    fun filterBasedOnParameters(
        sortType: SortTypeEnum,
        filters: List<String>
    ) {

        val selectedFilters = arrayListOf<String>()
        for (i in filters.indices) {
            if (categoryFlags[i])
                selectedFilters.add(filters[i])
        }
        filterCaseStudyData.value = (Pair(sortType, selectedFilters))
        performFilteringCaseStudies()
    }

    fun performFilteringCaseStudies() {
        val filteredCategories = filterCaseStudyData.value?.second
        var filteredData: List<CaseStudies> = when (filterCaseStudyData.value?.first) {
            SortTypeEnum.ASCENDING -> {
                allCaseStudies.sortedWith(compareBy { it.title })
            }
            SortTypeEnum.DESCENDING -> {
                allCaseStudies.sortedByDescending { it.title }
            }
            else -> {
                allCaseStudies
            }
        }

        if (filteredCategories?.isNotEmpty() == true) {
            filteredData =
                filteredData.filter { caseStudy -> filteredCategories.contains(caseStudy.vertical) }
        }
        favCaseStudyData.postValue(filteredData)
    }

    fun setUpFilterByCategories(categories: List<CaseStudies>) {
        categoryFlags = BooleanArray(categories.size)
        filterByCategories.postValue(
            categories.map { it.vertical }.toSet().toList() as List<String>
        )
    }

    fun clearFilters(){
        favCaseStudyData.postValue(allCaseStudies)
    }
}
