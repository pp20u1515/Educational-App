package com.example.programmingc.di

import com.example.programmingc.data.datasource.remote.service.INetworkDaoService
import com.example.programmingc.datasource.remote.dao.FirebaseAuthDao
import com.example.programmingc.datasource.remote.service.NetworkDaoService
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)  // Этот модуль будет использоваться на уровне всего приложения
class RemoteModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()  // Возвращаем экземпляр FirebaseAuth
    }

    @Provides
    fun provideFirebaseAuthDao(firebaseAuth: FirebaseAuth): FirebaseAuthDao {
        return FirebaseAuthDao(firebaseAuth)
    }

    @Provides
    fun provideNetworkDaoService(firebaseAuthDao: FirebaseAuthDao): INetworkDaoService {
        return NetworkDaoService(firebaseAuthDao)
    }
}