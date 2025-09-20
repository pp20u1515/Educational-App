package com.example.programmingc.data.repository

import com.example.programmingc.R
import com.example.programmingc.domain.model.Package
import com.example.programmingc.domain.repo.IPackageRepository
import javax.inject.Inject

class PackageRepository @Inject constructor(): IPackageRepository {
    override suspend fun showPackages(): List<Package> {
        return listOf(
            Package("Small Package", R.drawable.diamond, "200", "$1.99"),
            Package("Medium Package", R.drawable.diamond, "525", "$4.99"),
            Package("Large Package", R.drawable.diamond, "1125", "$9.99"),
            Package("X-Large Package", R.drawable.diamond, "2350", "$19.99"),
            Package("Super Package", R.drawable.diamond, "6250", "$49.99"),
            Package("Premium Package", R.drawable.diamond, "13500", "$99.99")
        )
    }
}