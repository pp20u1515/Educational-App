package com.example.programmingc.presentation.ui.objects.visiable_objects.menubar

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.programmingc.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseMenuBar: Fragment() {
    private val _viewModel: MainViewModel by activityViewModels()
    protected val menuViewModel: MainViewModel get() = _viewModel

    protected fun showMenu(){
        menuViewModel.menuBarVisible.value = true
    }

    protected fun hideMenu(){
        menuViewModel.menuBarVisible.value=false
    }

    override fun onResume() {
        super.onResume()

        if (shouldShowMenu()){
            showMenu()
        }
        else{
            hideMenu()
        }
    }
    // Абстрактный метод, который должен быть реализован в дочерних классах
    abstract fun shouldShowMenu(): Boolean
}