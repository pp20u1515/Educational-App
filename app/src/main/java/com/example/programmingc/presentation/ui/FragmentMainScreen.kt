package com.example.programmingc.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programmingc.R
import com.example.programmingc.databinding.FragmentMainScreenBinding
import com.example.programmingc.presentation.ui.adapter.LessonAdapter
import com.example.programmingc.presentation.ui.objects.visiable_objects.Lesson
import com.example.programmingc.presentation.ui.objects.visiable_objects.menubar.BaseMenuBar

class FragmentMainScreen: BaseMenuBar() {
    private var _binding: FragmentMainScreenBinding? = null
    private val binding: FragmentMainScreenBinding get() = _binding!!

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val lessons = listOf(
            Lesson(1, "Introduction", "Basics of C", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            Lesson(2, "Variables", "How to declare and use variables", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            Lesson(3, "Control Flow", "if, when, loops", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            Lesson(4, "Functions", "How to use functions", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            Lesson(5, "Classes", "All about classes", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow))
        )

        binding.lessonRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.lessonRecyclerView.adapter = LessonAdapter(lessons)

    }
}