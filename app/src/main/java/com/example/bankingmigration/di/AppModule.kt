package com.example.bankingmigration.di

import com.example.bankingmigration.core.result.AppDispatchers
import com.example.bankingmigration.core.result.DefaultAppDispatchers
import com.example.bankingmigration.data.repository.BankingRepositoryImpl
import com.example.bankingmigration.domain.repository.BankingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingsModule {
    @Binds
    @Singleton
    abstract fun bindBankingRepository(
        implementation: BankingRepositoryImpl,
    ): BankingRepository

    @Binds
    @Singleton
    abstract fun bindAppDispatchers(
        implementation: DefaultAppDispatchers,
    ): AppDispatchers
}

@Module
@InstallIn(SingletonComponent::class)
object AppProvidesModule {
    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
}
