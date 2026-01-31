package com.example.programmingc.presentation.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentCoursesBinding
import com.example.programmingc.presentation.ui.menu.BaseMenuBar

class FragmentCourses: BaseMenuBar() {
    private var _binding: FragmentCoursesBinding?= null
    private val binding: FragmentCoursesBinding get() = _binding!!

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonC.setOnClickListener{
            val direction = FragmentCoursesDirections.actionFragmentLevelsToFragmentCourseC()
            findNavController().navigate(direction)
        }

        binding.buttonNewCourse.setOnClickListener{
            Toast.makeText(requireContext(), "New course will appear soon!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}