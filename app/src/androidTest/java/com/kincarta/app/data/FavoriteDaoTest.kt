package com.kincarta.app.data

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.kincarta.app.data.source.local.AppDatabase
import com.kincarta.app.data.source.local.entity.FavoriteCaseStudyEntity
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
class FavoriteDaoTest {

    private lateinit var mDatabase: AppDatabase
    private lateinit var favCaseStudies: List<FavoriteCaseStudyEntity>

    @Before
    fun setUp() {
        mDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getTargetContext(),
            AppDatabase::class.java
        )
            .build()
        favCaseStudies = arrayListOf(FavoriteCaseStudyEntity(1, 1),
            FavoriteCaseStudyEntity(2, 1),
            FavoriteCaseStudyEntity(3, 0))

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        mDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun isCaseStudiesListEmpty() {
        assertEquals(0, mDatabase.favoritesDao.loadAll().size)
    }

    @Test
    @Throws(Exception::class)
    fun insertCaseStudies() {
        val caseStudy = FavoriteCaseStudyEntity(1, 1)
        var insertedCaseStudies : Long ?= null
        favCaseStudies.forEach {
            insertedCaseStudies = mDatabase.favoritesDao.insert(caseStudy)
        }
        assertNotNull(insertedCaseStudies)
    }

    @Test
    @Throws(Exception::class)
    fun insertCaseStudyAndLoadByTitle() {
        favCaseStudies.forEach {
            mDatabase.favoritesDao.insert(it)
        }

        val caseStudyEntity: FavoriteCaseStudyEntity
        runBlocking {
            caseStudyEntity = mDatabase.favoritesDao.loadFavoritesByCaseStudyId(1)
        }
        MatcherAssert.assertThat(caseStudyEntity, CoreMatchers.equalTo(favCaseStudies[0]))
    }

    @Test
    @Throws(Exception::class)
    fun retrievesCaseStudies() {
        favCaseStudies.forEach {
            mDatabase.favoritesDao.insert(it)
        }


        val loadedCaseStudies = mDatabase.favoritesDao.loadAll()
        assertEquals(favCaseStudies, loadedCaseStudies)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllCaseStudies() {
        mDatabase.caseStudyDao.deleteAll()
        val loadedAllCases = mDatabase.caseStudyDao.loadAll()
        assert(loadedAllCases.isEmpty())
    }
}