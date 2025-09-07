package com.example.programmingc.presentation.ui.objects.visiable_objects.menubar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.programmingc.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseMenuBar: Fragment() {
    private val _viewModel: MainViewModel by activityViewModels()
    protected val menuViewModel: MainViewModel get() = _viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateMenuVisibility()
    }

    override fun onStart() {
        super.onStart()
        updateMenuVisibility()
    }

    private fun updateMenuVisibility(){
        if (shouldShowMenu()){
            menuViewModel.showMenu()
        }
        else{
            menuViewModel.hideMenu()
        }
    }

    // Абстрактный метод, который должен быть реализован в дочерних классах
    abstract fun shouldShowMenu(): Boolean
}