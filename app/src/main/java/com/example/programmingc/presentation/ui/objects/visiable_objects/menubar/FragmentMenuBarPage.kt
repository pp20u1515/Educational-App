package com.example.programmingc.presentation.ui.objects.visiable_objects.menubar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentMenubarPageBinding
import com.example.programmingc.presentation.ui.MainViewModel

class FragmentMenuBarPage: BaseMenuBar() {
    private var _binding: FragmentMenubarPageBinding ?= null
    private val binding: FragmentMenubarPageBinding get() = _binding!!
    private lateinit var direction: NavDirections

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenubarPageBinding.inflate(inflater, container, false)
        menuViewModel.menuBarVisible.value = true

        binding.buttonCourses.setOnClickListener{
            direction = FragmentMenuBarPageDirections.actionFragmentMenuBarPageToFragmentMainScreen2()
            findNavController().navigate(direction)
        }

        binding.buttonDiamonds.setOnClickListener{
            direction = FragmentMenuBarPageDirections.actionFragmentMenuBarPageToFragmentDiamonds2()
            findNavController().navigate(direction)
        }

        binding.buttonSettings.setOnClickListener{
            Toast.makeText(requireContext(), "Soon", Toast.LENGTH_SHORT).show()
        }

        binding.buttonLevels.setOnClickListener{
            direction = FragmentMenuBarPageDirections.actionFragmentMenuBarPageToFragmentLevels()
            findNavController().navigate(direction)
        }

        binding.buttonLogOut.setOnClickListener{
            direction = FragmentMenuBarPageDirections.actionFragmentMenuBarPageToFragmentAuth()
            findNavController().navigate(direction)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}