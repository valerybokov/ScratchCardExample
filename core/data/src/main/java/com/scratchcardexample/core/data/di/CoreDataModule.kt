package com.scratchcardexample.core.data.di

import com.scratchcardexample.core.data.repository.AndroidSettings
import com.scratchcardexample.core.data.repository.ScratchCodeRepositoryImpl
import com.scratchcardexample.core.domain.repository.ScratchCodeRepository
import com.scratchcardexample.core.domain.repository.Settings
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Binds
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class CoreDataModule {
    companion object {
        @Provides
        @Singleton
        fun provideApplicationScope(): CoroutineScope {
            return CoroutineScope(SupervisorJob() + Dispatchers.IO)
        }
    }

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