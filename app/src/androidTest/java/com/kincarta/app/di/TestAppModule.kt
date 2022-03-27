package com.plcoding.cleanarchitecturenoteapp.di

import android.app.Application
import androidx.room.Room
import com.kincarta.app.data.repository.CaseStudiesRepositoryImpl
import com.kincarta.app.data.source.local.AppDatabase
import com.kincarta.app.domain.repository.ICaseStudiesRepository
import com.kincarta.app.source.remote.RetrofitService

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            AppDatabase::class.java,
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: AppDatabase, service: RetrofitService): ICaseStudiesRepository {
        return CaseStudiesRepositoryImpl(db, service)
    }

  /*  @Provides
    @Singleton
    fun provideCaseStudies(repository: ICaseStudiesRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }
*/
    
}