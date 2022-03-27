package com.kincarta.app.data

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.kincarta.app.data.source.local.AppDatabase
import com.kincarta.app.data.source.local.entity.CaseStudyEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(androidx.test.runner.AndroidJUnit4::class)
class CaseStudyDaoTest {

    private lateinit var mDatabase: AppDatabase
    private lateinit var caseStudies: List<CaseStudyEntity>

    @Before
    fun setUp() {
        mDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getTargetContext(),
            AppDatabase::class.java
        )
            .build()
        caseStudies = arrayListOf(CaseStudyEntity(1, title = "Case study 1"),
            CaseStudyEntity(2, title = "Case Study 2"),
            CaseStudyEntity(3, title = "Case Study 3"))

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        mDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun isCaseStudiesListEmpty() {
        assertEquals(0, mDatabase.caseStudyDao.loadAll().size)
    }

    @Test
    @Throws(Exception::class)
    fun insertCaseStudies() {
        //val caseStudies = TestUtil.makeCaseStudyList(7)
        val insertedCaseStudies = mDatabase.caseStudyDao.insertAllCaseStudies(caseStudies)
        assertNotNull(insertedCaseStudies)
    }

    @Test
    @Throws(Exception::class)
    fun insertCaseStudyAndLoadByTitle() {
        mDatabase.caseStudyDao.insertAllCaseStudies(caseStudies)

        val caseStudyEntity: CaseStudyEntity
        runBlocking {
            caseStudyEntity = mDatabase.caseStudyDao.loadCaseStudyById(1)
        }
        MatcherAssert.assertThat(caseStudyEntity, CoreMatchers.equalTo(caseStudies[0]))
    }

    @Test
    @Throws(Exception::class)
    fun retrievesCaseStudies() {
        mDatabase.caseStudyDao.insertAllCaseStudies(caseStudies)


        val loadedCaseStudies = mDatabase.caseStudyDao.loadAll()
        assertEquals(caseStudies, loadedCaseStudies)
    }

    @Test
    @Throws(Exception::class)
    fun deleteCaseStudy() {
        mDatabase.caseStudyDao.delete(caseStudies[0])

        val loadOneByCaseStudyId: CaseStudyEntity
        runBlocking {
            loadOneByCaseStudyId = mDatabase.caseStudyDao.loadCaseStudyById(1)
        }
        assertNull(loadOneByCaseStudyId)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllCaseStudies() {
        mDatabase.caseStudyDao.deleteAll()
        val loadedAllCases = mDatabase.caseStudyDao.loadAll()
        assert(loadedAllCases.isEmpty())
    }
}