package com.example.programmingc.presentation.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.programmingc.databinding.FragmentMenubarBinding
import com.example.programmingc.presentation.ui.MainActivity

class FragmentMenuBar: BaseMenuBar() {
    private var _binding: FragmentMenubarBinding?= null
    private val binding: FragmentMenubarBinding get() = _binding!!
    private var menuButtonClickListener: (() -> Unit)? = null

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

        viewModel.menuBarVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.menuBar.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        binding.menuIcon.setOnClickListener{
            (requireActivity() as MainActivity).openDrawer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        menuButtonClickListener = null
    }

    fun setOnMenuButtonClickListener(listener: () -> Unit) {
        menuButtonClickListener = listener
    }
}