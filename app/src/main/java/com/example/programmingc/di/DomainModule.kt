package com.example.programmingc.di

import com.example.programmingc.data.datasource.local.service.CredentialDaoService
import com.example.programmingc.data.datasource.local.service.UserDaoService
import com.example.programmingc.data.datasource.remote.service.INetworkDaoService
import com.example.programmingc.data.repository.CredentialRepository
import com.example.programmingc.data.repository.UserRepository
import com.example.programmingc.datasource.remote.service.NetworkDaoService
import com.example.programmingc.domain.repo.ICredentialRepository
import com.example.programmingc.domain.repo.IUserRepository
import com.example.programmingc.domain.usecase.AuthenticateUseCase
import com.example.programmingc.domain.usecase.GetUsersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    fun proviceUserRepository(
        userDaoService: UserDaoService,
        networkDaoService: NetworkDaoService
    ): IUserRepository{
        return UserRepository(userDaoService, networkDaoService)
    }

    @Provides
    fun provideCredentialRepository(
        credentialDaoService: CredentialDaoService,
        userDaoService: UserDaoService,
        networkDaoService: INetworkDaoService
    ): ICredentialRepository{
        return CredentialRepository(credentialDaoService, userDaoService, networkDaoService)
    }

    @Provides
    fun provideAuthenticateUseCase(
        credentialRepository: ICredentialRepository
    ): AuthenticateUseCase {
        return AuthenticateUseCase(credentialRepository)
    }

    @Provides
    fun provideGetUsersUseCase(
        userRepository: IUserRepository
    ): GetUsersUseCase {
        return GetUsersUseCase(userRepository)
    }
}