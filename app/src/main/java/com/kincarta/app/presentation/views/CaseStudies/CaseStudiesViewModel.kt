package com.kincarta.app.ui.views

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kincarta.app.data.mapper.toOriginal
import com.kincarta.app.domain.model.CaseStudies
import com.kincarta.app.domain.model.CaseStudiesResponse
import com.kincarta.app.domain.model.SortTypeEnum
import com.kincarta.app.domain.usecase.base.GetCaseStudiesUseCase
import com.kincarta.app.util.AlertUtil
import com.kincarta.app.util.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**A helper class for the UI controller that is responsible for
 * preparing data for the UI [CaseStudiesFragment]
 *
 * @author Debojyoti
 * */
@HiltViewModel
class CaseStudiesViewModel @Inject constructor(
    private val caseStudiesUseCase: GetCaseStudiesUseCase
) : ViewModel() {

    val caseStudyListReceivedLiveData = MutableLiveData<CaseStudiesResponse>()
    val isLoad = MutableLiveData<Boolean>()
    val areAllDataSaved = MutableLiveData<Boolean>()
    val filterByCategories = MutableLiveData<List<String>>()
    lateinit var categoryFlags: BooleanArray
    val filterCaseStudyData = MutableLiveData<Pair<SortTypeEnum, List<String>>>()
    lateinit var allCaseStudies: CaseStudiesResponse

    init {
        isLoad.value = false
    }

    fun loadCaseStudies() {
        caseStudiesUseCase.execute(
            onSuccess = {
                isLoad.value = true
                caseStudyListReceivedLiveData.value = it
                allCaseStudies = it
                setUpFilterByCategories(it.caseStudies)
                cacheCaseStudies()
            },
            onError = {
                isLoad.value = true
                caseStudyListReceivedLiveData.value = CaseStudiesResponse(listOf())
            }
        )
    }

    fun cacheCaseStudies() {
        CoroutineScope(Dispatchers.IO).launch {
            val isCached =
                caseStudiesUseCase.saveCaseStudies(caseStudyListReceivedLiveData.value)
            areAllDataSaved.postValue(isCached)
        }
    }

    fun getCachedCaseStudies(){
        CoroutineScope(Dispatchers.IO).launch {
            val allCaseStudies : List<CaseStudies> = caseStudiesUseCase.loadAllCaseStudiesFromDB().map { it.toOriginal() }
            caseStudyListReceivedLiveData.postValue(CaseStudiesResponse(allCaseStudies))
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
        //val selectedFilters = filters.forEach { it ==  booleanArray[]}
        filterCaseStudyData.value = (Pair(sortType, selectedFilters))
        performFilteringCaseStudies()
    }

    fun performFilteringCaseStudies() {
        val filteredCategories = filterCaseStudyData.value?.second
        var filteredData: List<CaseStudies> = when (filterCaseStudyData.value?.first) {
            SortTypeEnum.ASCENDING -> {
                allCaseStudies.caseStudies.sortedWith(compareBy { it.title })
            }
            SortTypeEnum.DESCENDING -> {
                allCaseStudies.caseStudies.sortedByDescending { it.title }
            }
            else -> {
                allCaseStudies.caseStudies
            }
        }

        if (filteredCategories?.isNotEmpty() == true) {
            filteredData =
                filteredData.filter { caseStudy -> filteredCategories.contains(caseStudy.vertical) }
        }
        caseStudyListReceivedLiveData.postValue(CaseStudiesResponse(filteredData))
    }

    fun setUpFilterByCategories(categories: List<CaseStudies>) {
        categoryFlags = BooleanArray(categories.size)
        filterByCategories.postValue(
            categories.map { it.vertical }.toSet().toList() as List<String>
        )
    }

    fun clearFilters(){
        caseStudyListReceivedLiveData.postValue(allCaseStudies)
    }

    companion object {
        private val TAG = CaseStudiesViewModel::class.java.simpleName
    }
}