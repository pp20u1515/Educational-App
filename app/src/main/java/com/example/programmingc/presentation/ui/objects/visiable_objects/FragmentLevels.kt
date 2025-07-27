package com.example.programmingc.presentation.ui.objects.visiable_objects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentLevelsBinding
import com.example.programmingc.presentation.ui.MainViewModel
import com.example.programmingc.presentation.ui.objects.visiable_objects.menubar.BaseMenuBar

class FragmentLevels: BaseMenuBar() {
    private var _binding: FragmentLevelsBinding ?= null
    private val binding: FragmentLevelsBinding get() = _binding!!
    private lateinit var direction: NavDirections

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLevelsBinding.inflate(inflater, container, false)
        menuViewModel.menuBarVisible.value = true

        binding.buttonBeginner.setOnClickListener{
            direction = FragmentLevelsDirections.actionFragmentLevelsToFragmentMainScreen()
            findNavController().navigate(direction)
        }

        binding.buttonMiddle.setOnClickListener{
            Toast.makeText(requireContext(), "Soon", Toast.LENGTH_SHORT).show()
        }

        binding.buttonPro.setOnClickListener{
            Toast.makeText(requireContext(), "Soon", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}