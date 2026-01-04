package com.example.programmingc.presentation.ui.objects.visiable_objects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentCoursesBinding
import com.example.programmingc.presentation.ui.menu.BaseMenuBar

class FragmentCourses: BaseMenuBar() {
    private var _binding: FragmentCoursesBinding ?= null
    private val binding: FragmentCoursesBinding get() = _binding!!
    private lateinit var direction: NavDirections

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)

        binding.buttonBeginner.setOnClickListener{
            direction = FragmentCoursesDirections.actionFragmentLevelsToFragmentMainScreen()
            findNavController().navigate(direction)
        }

        binding.buttonMiddle.setOnClickListener{
            Toast.makeText(requireContext(), "Soon", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}