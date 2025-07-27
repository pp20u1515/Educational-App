package com.example.programmingc.presentation.ui.objects.visiable_objects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.example.programmingc.databinding.FragmentMenubarBinding
import com.example.programmingc.presentation.ui.MainActivity
import com.example.programmingc.presentation.ui.MainViewModel
import com.example.programmingc.presentation.ui.objects.visiable_objects.menubar.BaseMenuBar

class FragmentMenuBar: BaseMenuBar() {
    private var _binding: FragmentMenubarBinding ?= null
    private val binding: FragmentMenubarBinding get() = _binding!!
    private lateinit var direction: NavDirections

    override fun shouldShowMenu(): Boolean {
        return false // Сам меню-бар не должен управлять своей видимостью
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenubarBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuViewModel.menuBarVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.menuBar.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        binding.menuBar.setOnClickListener{
            (requireActivity() as MainActivity).openDrawer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}