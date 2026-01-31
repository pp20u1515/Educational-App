package com.example.programmingc.presentation.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.programmingc.databinding.FragmentMenubarBinding
import com.example.programmingc.presentation.ui.MainActivity
import com.example.programmingc.presentation.ui.MainViewModel
import kotlinx.coroutines.launch

class FragmentMenuBar: BaseMenuBar() {
    private var _binding: FragmentMenubarBinding?= null
    private val binding: FragmentMenubarBinding get() = _binding!!
    private val menuBarViewModel: MenuBarViewModel by activityViewModels()

    override fun shouldShowMenu(): Boolean {
        return false
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

        setupClickListener()
        observeViewModel()
    }

    private fun setupClickListener(){
        binding.menuIcon.setOnClickListener{
            (requireActivity() as MainActivity).openDrawer()
        }

        binding.cardView.setOnClickListener {
            viewModel.navigateTo(MainViewModel.MenuNavigationEvent.ToDiamonds)
        }

        binding.menuHint.setOnClickListener {
            viewModel.navigateTo(MainViewModel.MenuNavigationEvent.ToLives)
        }
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                menuBarViewModel.liveCount.collect { count ->
                    updateHintCountUI(count)
                }
            }
        }
    }

    private fun updateHintCountUI(count: Int){
        binding.hintCount.text = count.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}