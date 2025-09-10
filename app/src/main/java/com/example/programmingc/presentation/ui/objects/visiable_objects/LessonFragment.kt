package com.example.programmingc.presentation.ui.objects.visiable_objects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programmingc.databinding.LessonBinding
import com.example.programmingc.presentation.ui.adapter.LessonAdapter
import com.example.programmingc.presentation.ui.auth.AuthFragmentDirections
import com.example.programmingc.presentation.ui.objects.visiable_objects.menubar.BaseMenuBar
import dagger.hilt.android.AndroidEntryPoint
import com.example.programmingc.R
import com.example.programmingc.databinding.FragmentLessonsBinding
import kotlinx.coroutines.flow.observeOn

@AndroidEntryPoint
class LessonFragment: BaseMenuBar() {
    private var _binding: FragmentLessonsBinding? = null
    private val binding: FragmentLessonsBinding get() = _binding!!
    private val lessonViewModel: LessonViewModel by viewModels()
    private lateinit var adapter: LessonAdapter

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lessonId = arguments?.getString("lessonId") ?: ""
        println("lessonId = $lessonId")
        lessonViewModel.loadLesson(lessonId)
        setupAdapter()
        setupObservers()
    }

    private fun setupObservers(){
        lessonViewModel.currentLesson.observe(viewLifecycleOwner) { lesson ->
            lesson?.let{
                adapter.updateData(listOf(it))
            }
        }
    }

    private fun setupAdapter(){
        adapter = LessonAdapter(emptyList()){ lessonId ->
            navigateToPractice(lessonId)
        }

        binding.lessonRecyclerViewId.layoutManager = LinearLayoutManager(requireContext())
        binding.lessonRecyclerViewId.adapter = adapter
    }
    private fun navigateToPractice(id: String){
        val actionId = R.id.action_fragment_lesson_to_fragment_practice
        val bundle = Bundle().apply {
            putString("lessonId", id)
        }
        /*val direction = LessonFragmentDirections
            .actionFragmentLessonToFragmentPractice()
            .setLessonId(id)*/
        findNavController().navigate(actionId, bundle)
    }
}