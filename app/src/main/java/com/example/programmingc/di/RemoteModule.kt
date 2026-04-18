package com.example.programmingc.di

import com.example.programmingc.data.source.remote.service.INetworkDataSource
import com.example.programmingc.data.source.remote.service.NetworkDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
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
    fun provideNetworkDataSource(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase
    ): INetworkDataSource {
        return NetworkDataSource(firebaseAuth, firebaseDatabase)
    }
}