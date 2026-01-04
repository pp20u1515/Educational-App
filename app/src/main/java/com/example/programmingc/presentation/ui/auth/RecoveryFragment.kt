package com.example.programmingc.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentRecoverBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecoveryFragment: Fragment() {
    private var _binding: FragmentRecoverBinding? = null
    private val binding: FragmentRecoverBinding get() = _binding!!
    private val viewModel: RecoveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverBinding.inflate(inflater,container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListener()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListener(){
        binding.buttonNext.setOnClickListener{
            val email = binding.email.text.toString().trim()

            viewModel.resetPassword(email)
        }
    }

    private fun observeState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.recoveryState.collect { state ->
                    handleState(state)
                }
            }
        }
    }

    private fun handleState(state: RecoveryViewModel.RecoveryState){
        when (state){
            is RecoveryViewModel.RecoveryState.Idle -> {}
            is RecoveryViewModel.RecoveryState.Success -> {
                navigateDirection()
            }
            is RecoveryViewModel.RecoveryState.Error -> {
                showError(state.message)
            }
        }
    }

    private fun showError(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun navigateDirection(){
        val direction = RecoveryFragmentDirections.actionFragmentRecoverToFragmentAuth()
        findNavController().navigate(direction)
    }
}