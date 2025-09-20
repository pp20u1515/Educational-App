package com.example.programmingc.domain.repo

import com.example.programmingc.domain.model.Package

interface IPackageRepository {
    suspend fun showPackages(): List<Package>
}