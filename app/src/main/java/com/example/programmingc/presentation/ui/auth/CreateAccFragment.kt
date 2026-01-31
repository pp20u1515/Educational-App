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
import com.example.programmingc.databinding.FragmentCreateAccBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateAccFragment: Fragment() {
    private var _binding: FragmentCreateAccBinding? = null
    private val binding: FragmentCreateAccBinding get() = _binding!!
    private val viewModel: CreateAccViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListener()
        observeAuthState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListener(){
        binding.buttonNext.setOnClickListener{
            val email = binding.emailCreateAcc.text.toString().trim()
            val password = binding.passwordCreateAcc.text.toString().trim()

            viewModel.createAcc(email, password)
        }

        binding.textViewLogIn.setOnClickListener{
            navigateDirection()
        }
    }

    private fun observeAuthState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.authState.collect { state ->
                    handleAuthState(state)
                }
            }
        }
    }

    private fun handleAuthState(state: CreateAccViewModel.AuthState){
        when (state){
            is CreateAccViewModel.AuthState.Idle -> {

            }
            is CreateAccViewModel.AuthState.Success -> {
                navigateDirection()
            }
            is CreateAccViewModel.AuthState.ValidationError -> {
                showError(state.message)
            }
            is CreateAccViewModel.AuthState.Error -> {
                showError(state.message)
            }
        }
    }

    private fun showError(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun navigateDirection(){
        val direction = CreateAccFragmentDirections.actionFragmentCreateAccToFragmentAuth()
        findNavController().navigate(direction)
    }
}