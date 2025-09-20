package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.Package
import com.example.programmingc.domain.repo.IPackageRepository
import javax.inject.Inject

class ShowPackageUseCase @Inject constructor(
    private val packageRepository: IPackageRepository
) {
    suspend operator fun invoke(): List<Package>{
        return packageRepository.showPackages()
    }
}