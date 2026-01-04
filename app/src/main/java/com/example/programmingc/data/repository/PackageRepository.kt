package com.example.programmingc.data.repository

import com.example.programmingc.R
import com.example.programmingc.domain.model.Package
import com.example.programmingc.domain.repo.IPackageRepository
import javax.inject.Inject

class PackageRepository @Inject constructor(): IPackageRepository {
    override suspend fun showPackages(): List<Package> {
        return listOf(
            Package("Small Package", R.drawable.diamond, "200", "$0.99"),
            Package("Medium Package", R.drawable.diamond, "525", "$1.99"),
            Package("Large Package", R.drawable.diamond, "1125", "$2.99"),
            Package("X-Large Package", R.drawable.diamond, "2350", "$5.99"),
            Package("Super Package", R.drawable.diamond, "6250", "$8.99"),
            Package("Premium Package", R.drawable.diamond, "13500", "$10.99")
        )
    }
}