package com.example.programmingc.presentation.ui.menu

import com.example.programmingc.domain.repo.IHintsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuBarViewModel @Inject constructor(
    private val getDailyHintsUseCase: IHintsRepository
) {
    fun onMenuIconClick(){

    }
}