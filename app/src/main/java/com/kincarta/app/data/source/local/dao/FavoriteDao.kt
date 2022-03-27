package com.kincarta.app.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kincarta.app.data.source.local.entity.FavoriteCaseStudyEntity

/**
 * it provides access to [Favorite Case Studies] underlying database
 * */
@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(section: FavoriteCaseStudyEntity): Long

    @Query("SELECT * FROM Favorite")
    fun loadAll(): List<FavoriteCaseStudyEntity>

    @Query("DELETE FROM Favorite")
    fun deleteAll()

    @Query("SELECT * FROM Favorite where id = :caseStudyId")
    suspend fun loadFavoritesByCaseStudyId(caseStudyId: Int): FavoriteCaseStudyEntity
/*
    @Update
    fun update(entity: FavoriteCaseStudyEntity)*/
}
