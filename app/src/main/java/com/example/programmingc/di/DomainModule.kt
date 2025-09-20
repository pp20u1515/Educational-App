package com.example.programmingc.di

import android.app.Application
import com.example.programmingc.data.datasource.local.service.CredentialDaoService
import com.example.programmingc.data.datasource.local.service.UserDaoService
import com.example.programmingc.data.datasource.remote.service.INetworkDaoService
import com.example.programmingc.data.repository.AuthRepository
import com.example.programmingc.data.repository.CredentialRepository
import com.example.programmingc.data.repository.UserRepository
import com.example.programmingc.datasource.remote.service.NetworkDaoService
import com.example.programmingc.domain.repo.IAuthRepository
import com.example.programmingc.domain.repo.ICredentialRepository
import com.example.programmingc.domain.repo.IUserRepository
import com.example.programmingc.domain.usecase.AuthenticateUseCase
import com.example.programmingc.domain.usecase.GetUsersUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import android.content.Context
import com.example.programmingc.data.repository.LessonRepository
import com.example.programmingc.data.repository.PackageRepository
import com.example.programmingc.domain.repo.ILessonRepository
import com.example.programmingc.domain.repo.IPackageRepository
import com.example.programmingc.domain.usecase.CheckAuthStateUseCase
import com.example.programmingc.domain.usecase.CompleteLessonUseCase
import com.example.programmingc.domain.usecase.CreateAccUseCase
import com.example.programmingc.domain.usecase.GetLessonUseCase
import com.example.programmingc.domain.usecase.ResetPasswordUseCase
import com.example.programmingc.domain.usecase.ShowLessonsUseCase
import com.example.programmingc.domain.usecase.ShowPackageUseCase
import com.example.programmingc.domain.usecase.SignOutUseCase

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun provideUserRepository(
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
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): IAuthRepository{
        return AuthRepository(firebaseAuth)
    }

    @Provides
    fun provideLessonRepository(context: Context): ILessonRepository{
        return LessonRepository(context)
    }

    @Provides
    fun providePackageRepository(): IPackageRepository{
        return PackageRepository()
    }

    @Provides
    fun provideAuthenticateUseCase(
        credentialRepository: ICredentialRepository
    ): AuthenticateUseCase {
        return AuthenticateUseCase(credentialRepository)
    }

    @Provides
    fun provideCheckAuthStateUseCase(
        authRepository: IAuthRepository
    ): CheckAuthStateUseCase{
        return CheckAuthStateUseCase(authRepository)
    }

    @Provides
    fun provideCompleteLessonUseCase(
        lessonRepository: ILessonRepository
    ): CompleteLessonUseCase{
        return CompleteLessonUseCase(lessonRepository)
    }

    @Provides
    fun provideCreateAccUseCase(
        credentialRepository: ICredentialRepository
    ): CreateAccUseCase{
        return CreateAccUseCase(credentialRepository)
    }

    @Provides
    fun provideGetUsersUseCase(
        userRepository: IUserRepository
    ): GetUsersUseCase {
        return GetUsersUseCase(userRepository)
    }
    @Provides
    fun provideGetLessonUseCase(
        lessonRepository: ILessonRepository
    ): GetLessonUseCase{
        return GetLessonUseCase(lessonRepository)
    }

    @Provides
    fun provideResetPasswordUseCase(
        credentialRepository: ICredentialRepository
    ): ResetPasswordUseCase{
        return ResetPasswordUseCase(credentialRepository)
    }

    @Provides
    fun provideShowLessonsUseCase(
        lessonRepository: ILessonRepository
    ): ShowLessonsUseCase{
        return ShowLessonsUseCase(lessonRepository)
    }

    @Provides
    fun provideShowPackageUseCase(
        packageRepository: IPackageRepository
    ): ShowPackageUseCase{
        return ShowPackageUseCase(packageRepository)
    }

    @Provides
    fun provideSignOutUseCase(
        authRepository: IAuthRepository
    ): SignOutUseCase{
        return SignOutUseCase(authRepository)
    }
}