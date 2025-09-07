package com.example.programmingc.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programmingc.R
import com.example.programmingc.databinding.FragmentMainScreenBinding
import com.example.programmingc.domain.model.LessonWindow
import com.example.programmingc.presentation.ui.adapter.LessonWindowAdapter
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
        super.onViewCreated(view, savedInstanceState)


        val lessons = listOf(
            LessonWindow("1", 1, "Introduction", "Basics of C", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("2", 2, "Variables", "How to declare and use variables", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("3", 3, "Control Flow", "if, when, loops", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("4", 4, "Functions", "How to use functions", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("5", 5, "Classes", "All about classes", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow))
        )

        binding.lessonRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.lessonRecyclerView.adapter = LessonWindowAdapter(lessons){ lessonId ->
            onPlayButtonClicked(lessonId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onPlayButtonClicked(lessonId: String){
        navigateToLesson(lessonId)
    }

    private fun navigateToLesson(id: String){
        /*val actionId = R.id.action_fragmentMainScreen_to_fragment_lesson
        val bundle = Bundle().apply {
            putString("lessonId", id)
        }*/
        val direction = FragmentMainScreenDirections
            .actionFragmentMainScreenToFragmentLesson(lessonId = id)
        findNavController().navigate(direction)
    }
}
