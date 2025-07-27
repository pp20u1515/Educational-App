package com.example.programmingc.presentation.ui.objects.visiable_objects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.programmingc.databinding.FragmentDiamondsBinding
import com.example.programmingc.presentation.ui.MainViewModel
import com.example.programmingc.presentation.ui.objects.visiable_objects.menubar.BaseMenuBar

class FragmentDiamonds: BaseMenuBar() {
    private var _binding: FragmentDiamondsBinding ?= null
    private val binding: FragmentDiamondsBinding get() = _binding!!

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiamondsBinding.inflate(inflater, container, false)
        menuViewModel.menuBarVisible.value = true


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}