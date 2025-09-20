package com.example.programmingc.presentation.ui.diamonds_products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.model.Package
import com.example.programmingc.domain.usecase.ShowPackageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PackageViewModel @Inject constructor(
    private val showPackageUseCase: ShowPackageUseCase
): ViewModel() {
    private val _package = MutableLiveData<List<Package>>()
    val storedPackage: LiveData<List<Package>> = _package

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun showPackages(){
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val packagesList = showPackageUseCase.invoke()
                _package.value = packagesList
            } catch (e: Exception){
                _errorMessage.value = "Error loading packages"
            }
        }
    }
    fun clearError(){
        _errorMessage.value = null
    }
}