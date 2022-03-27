package com.kincarta.app.data

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.kincarta.app.data.source.local.AppDatabase
import com.kincarta.app.data.source.local.entity.SectionEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(androidx.test.runner.AndroidJUnit4::class)
class SectionDaoTest {

    private lateinit var mDatabase: AppDatabase
    private lateinit var sections: List<SectionEntity>

    @Before
    fun setUp() {
        mDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getTargetContext(),
            AppDatabase::class.java
        )
            .build()
        sections = arrayListOf(
            SectionEntity(1, 1, "Title 1"),
            SectionEntity(2, 2, "Title 2"),
            SectionEntity(3, 3, "Title 3")
        )

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        mDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun isSectionsListEmpty() {
        assertEquals(0, mDatabase.sectionDao.loadAll().size)
    }

    @Test
    @Throws(Exception::class)
    fun insertSections() {
        val insertedSections = mDatabase.sectionDao.insertAllSections(sections)
        assertNotNull(insertedSections)
    }

    @Test
    @Throws(Exception::class)
    fun insertCaseStudyAndLoadByTitle() {
        mDatabase.sectionDao.insertAllSections(sections)
        val actualSections = sections.filter { it.id == 1 }
        val sectionEntity: List<SectionEntity>
        runBlocking {
            sectionEntity = mDatabase.sectionDao.loadSectionsByCaseStudyId(1)
        }
        MatcherAssert.assertThat(sectionEntity, CoreMatchers.equalTo(actualSections))
    }

    @Test
    @Throws(Exception::class)
    fun retrievesCaseStudies() {
        mDatabase.sectionDao.insertAllSections(sections)
        val loadedCaseStudies = mDatabase.sectionDao.loadAll()
        assertEquals(sections, loadedCaseStudies)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllCaseStudies() {
        mDatabase.sectionDao.deleteAll()
        val loadedAllCases = mDatabase.sectionDao.loadAll()
        assert(loadedAllCases.isEmpty())
    }
}