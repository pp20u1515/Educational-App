package com.example.programmingc.di

import android.content.Context
import androidx.room.Room
import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.service.CredentialDaoService
import com.example.programmingc.data.datasource.local.service.UserDaoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "educationalApp.db"
        ).build()
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