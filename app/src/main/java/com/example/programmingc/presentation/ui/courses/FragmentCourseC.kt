package com.example.programmingc.presentation.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programmingc.databinding.FragmentMainScreenBinding
import com.example.programmingc.presentation.ui.adapter.LessonWindowAdapter
import com.example.programmingc.presentation.ui.menu.BaseMenuBar

class FragmentCourseC: BaseMenuBar() {
    private var _binding: FragmentMainScreenBinding? = null
    private val binding: FragmentMainScreenBinding get() = _binding!!
    private val courseCViewModel: CourseCViewModel by viewModels()
    private lateinit var adapter: LessonWindowAdapter

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

        setupRecyclerView() // ← Добавляем инициализацию RecyclerView
        observeViewModel()
        courseCViewModel.loadLessons()
    }

    private fun setupRecyclerView() {
        // Инициализируем адаптер с пустым списком
        adapter = LessonWindowAdapter(emptyList()) { lessonId ->
            onPlayButtonClicked(lessonId)
        }
        binding.lessonRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.lessonRecyclerView.adapter = adapter // ← Устанавливаем адаптер!
    }

    private fun observeViewModel(){
        courseCViewModel.lessons.observe(viewLifecycleOwner){ lessons ->
            adapter.updateLessons(lessons) // ← Обновляем данные существующего адаптера
        }

        courseCViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let { message ->
                showErrorToast(message)
                courseCViewModel.clearError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showErrorToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun onPlayButtonClicked(lessonId: String){
        navigateToLesson(lessonId)
    }

    private fun navigateToLesson(id: String){
        val direction = FragmentCourseCDirections
            .actionFragmentCourseCToFragmentLesson(lessonId = id)
        findNavController().navigate(direction)
    }
}