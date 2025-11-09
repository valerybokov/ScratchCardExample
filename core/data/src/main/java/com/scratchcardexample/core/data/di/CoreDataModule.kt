package com.scratchcardexample.core.data.di

import com.scratchcardexample.core.data.repository.AndroidSettings
import com.scratchcardexample.core.data.repository.ScratchCodeRepositoryImpl
import com.scratchcardexample.core.data.repository.interfaces.ScratchCodeRepository
import com.scratchcardexample.core.data.repository.interfaces.Settings
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Binds
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class CoreDataModule {
    @Binds
    @Singleton
    abstract fun bindScratchCodeRepository(
        repo: ScratchCodeRepositoryImpl): ScratchCodeRepository

    @Binds
    @Singleton
    abstract fun bindSettings(
        settings: AndroidSettings
    ): Settings
}