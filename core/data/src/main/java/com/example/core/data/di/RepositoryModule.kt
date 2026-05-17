package com.example.core.data.di

import com.example.core.data.repository.FakeNoteRepository
import com.example.core.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        fakeNoteRepository: FakeNoteRepository
    ): NoteRepository
}
