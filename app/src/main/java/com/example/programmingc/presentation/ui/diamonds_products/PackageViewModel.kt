package com.example.programmingc.presentation.ui.diamonds_products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.model.Package
import com.example.programmingc.domain.usecase.ShowPackageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PackageViewModel @Inject constructor(
    private val showPackageUseCase: ShowPackageUseCase
): ViewModel() {
    private val _storedPackage = MutableSharedFlow<List<Package>>(replay = 1)
    val storedPackage: SharedFlow<List<Package>> = _storedPackage.asSharedFlow()

    private val _errorMessage = MutableSharedFlow<String?>(replay = 1)
    val errorMessage: SharedFlow<String?> = _errorMessage.asSharedFlow()

    fun showPackages(){
        viewModelScope.launch {
            _errorMessage.emit(null)

            try {
                val packagesList = showPackageUseCase.invoke()
                _storedPackage.emit(packagesList)
            } catch (e: Exception){
                _errorMessage.emit("Error loading packages")
            }
        }
    }

    suspend fun clearError(){
        _errorMessage.emit(null)
    }
}