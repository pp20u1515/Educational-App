package com.example.programmingc.di

import com.example.programmingc.data.datasource.remote.service.INetworkDaoService
import com.example.programmingc.datasource.remote.api.INetworkApi
import com.example.programmingc.datasource.remote.dao.NetworkDao
import com.example.programmingc.datasource.remote.service.NetworkDaoService
import com.example.programmingc.utils.Retrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {
    @Singleton
    @Provides
    fun proviteNetworkApi(): INetworkApi{
        val retrofit = Retrofit.Builder()
            .baseUrl("localhost")
            .build()
        return retrofit.create(INetworkApi::class.java)
    }

    @Provides
    fun provideNetworkDao(networkApi: INetworkApi): NetworkDao {
        return NetworkDao(networkApi)
    }

    @Provides
    fun provideNetworkDaoService(networkDao: NetworkDao): INetworkDaoService {
        return NetworkDaoService(networkDao)
    }
}