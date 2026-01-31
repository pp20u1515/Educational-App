package com.example.programmingc.presentation.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.programmingc.databinding.FragmentLivesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DialogFragmentLives: DialogFragment() {
    private var _binding: FragmentLivesBinding ?= null
    private val binding: FragmentLivesBinding get() = _binding!!
    private val viewModel: LivesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLivesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListener()
        observeTime()
        observeLives()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun observeTime(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.timeUntilReset.collect { data ->
                    updateTimerUI(data)
                }
            }
        }
    }

    private fun updateTimerUI(data: String){
        binding.resetTimeId.text = data
    }

    private fun observeLives(){
        lifecycleScope.launch {
            try {
                val lives = viewModel.checkAvailableLives()
                updateUI(lives)
            } catch (e: Exception){
                updateUI(0)
            }
        }
    }

    private fun updateUI(lives: Int){
        binding.availableLivesId.text = lives.toString()
    }

    private fun setupClickListener(){
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}