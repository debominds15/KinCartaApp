package com.kincarta.app.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.kincarta.app.data.mapper.toEntity
import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import com.kincarta.app.domain.model.CaseStudies
import com.kincarta.app.domain.usecase.base.GetCaseStudiesUseCase
import com.kincarta.app.feature_note.data.repository.FakeCaseStudiesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetCaseStudiesUseCaseTest {

    private lateinit var getCaseStudies: GetCaseStudiesUseCase
    private lateinit var fakeRepository: FakeCaseStudiesRepository
    var caseStudiesData = mutableListOf<CaseStudyEntity>()

    @Before
    fun setUp() {
        fakeRepository = FakeCaseStudiesRepository()
        getCaseStudies = GetCaseStudiesUseCase(fakeRepository)


        ('a'..'z').forEachIndexed { index, c ->
            caseStudiesData.add(
                CaseStudies(
                    id = index,
                    title = c.toString()
                ).toEntity()
            )
        }
        //caseStudiesData.shuffle()
        runBlocking {
            //getCaseStudies.saveCaseStudies(caseStudiesData, listOf())
            fakeRepository.saveAllCaseStudies(caseStudiesData, listOf())
        }
    }

    @Test
    fun `Check case studies for null`(){
        val caseStudiesResponse = fakeRepository.getAllCaseStudies().blockingGet()
        assertThat(caseStudiesResponse).apply {
            isNotNull()
        }
    }

    @Test
    fun `Check case studies sent and received are same`(){
        val caseStudiesResponse = getCaseStudies.buildUseCaseSingle().blockingGet().caseStudies.map { it.toEntity() }
        assertThat(caseStudiesResponse).isEqualTo(caseStudiesData)
    }
  /*  @Test
    fun `Check case studies for null`() {
        //val caseStudiesResponse = getCaseStudies.buildUseCaseSingle().blockingGet()
        val caseStudiesResponse = fakeRepository.getAllCaseStudies().blockingGet()
        assertThat(caseStudiesResponse).apply {
            isNotNull()

    }*/
/*
    @Test
    fun `Check case studies sent and received are same`()  {
        //val caseStudiesResponse = getCaseStudies.buildUseCaseSingle().blockingGet()
        val caseStudiesResponse = fakeRepository.caseStudiesData.map { it.toEntity() }
        //assertThat(caseStudiesResponse).isEqualTo(caseStudiesData)
        assertThat(caseStudiesResponse).isEqualTo(caseStudiesData)
        asser

    }*/

    /*@Test
    fun `Order notes by title descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Title(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].title).isGreaterThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by date ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Ascending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isLessThan(notes[i+1].timestamp)
        }
    }

    @Test
    fun `Order notes by date descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isGreaterThan(notes[i+1].timestamp)
        }
    }

    @Test
    fun `Order notes by color ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Color(OrderType.Ascending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].color).isLessThan(notes[i+1].color)
        }
    }

    @Test
    fun `Order notes by color descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Color(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].color).isGreaterThan(notes[i+1].color)
        }
    }*/
}