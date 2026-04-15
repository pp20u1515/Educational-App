package com.example.programmingc.di

import com.example.programmingc.data.source.remote.service.INetworkDaoService
import com.example.programmingc.data.source.remote.dao.FirebaseAuthDao
import com.example.programmingc.data.source.remote.dao.FirebaseStorageDao
import com.example.programmingc.data.source.remote.service.NetworkDaoService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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
    @Singleton
    fun provideFirebaseFirestore(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    fun provideFirebaseAuthDao(firebaseAuth: FirebaseAuth): FirebaseAuthDao {
        return FirebaseAuthDao(firebaseAuth)
    }

    @Provides
    fun provideNetworkDaoService(
        firebaseAuthDao: FirebaseAuthDao,
        firebaseStorageDao: FirebaseStorageDao
    ): INetworkDaoService {
        return NetworkDaoService(firebaseAuthDao, firebaseStorageDao)
    }

    @Provides
    fun provideFirebaseStorageDao(firebaseStorage: FirebaseDatabase): FirebaseStorageDao{
        return FirebaseStorageDao(firebaseStorage)
    }
}