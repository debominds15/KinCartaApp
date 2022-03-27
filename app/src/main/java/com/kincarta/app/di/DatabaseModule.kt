package com.kincarta.app.di

import android.app.Application
import androidx.room.Room
import com.kincarta.app.data.source.local.AppDatabase
import com.kincarta.app.data.source.local.dao.CaseStudyDao
import com.kincarta.app.data.source.local.dao.FavoriteDao
import com.kincarta.app.data.source.local.dao.SectionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    internal fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).allowMainThreadQueries().build()
    }

    @Provides
    internal fun provideCaseStudyDao(appDatabase: AppDatabase): CaseStudyDao {
        return appDatabase.caseStudyDao
    }

    @Provides
    internal fun provideSectionDao(appDatabase: AppDatabase): SectionDao {
        return appDatabase.sectionDao
    }

    @Provides
    internal fun provideFavoritesDao(appDatabase: AppDatabase): FavoriteDao {
        return appDatabase.favoritesDao
    }
}
