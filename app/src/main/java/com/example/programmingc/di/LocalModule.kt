package com.example.programmingc.di

import com.example.databasedependencies.DatabaseDependencies
import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.service.CredentialDaoService
import com.example.programmingc.data.datasource.local.service.UserDaoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule: DatabaseDependencies {
    @Provides
    @Singleton
    override fun provideDataBase(): Database {
        return Database()
    }

    @Provides
    fun provideUserDaoService(database: Database): UserDaoService {
        return UserDaoService(database)
    }

    @Provides
    fun provideCredentialDaoService(database: Database): CredentialDaoService {
        return CredentialDaoService(database)
    }
}