package com.example.programmingc.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.example.programmingc.databinding.FragmentAuthBinding
import com.example.programmingc.presentation.ui.menu.BaseMenuBar
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment: BaseMenuBar() {
    private var _binding: FragmentAuthBinding? = null
    private val binding: FragmentAuthBinding  get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
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
        binding.logIn.setOnClickListener {
            val email = binding.clientEmail.text.toString().trim()
            val password = binding.clientPassword.text.toString().trim()

            authViewModel.onLogInClick(email, password)
        }

        binding.createNewAccount.setOnClickListener {
            val direction = AuthFragmentDirections.actionFragmentAuthToFragmentCreateAcc()
            findNavController().navigate(direction)
        }

        binding.forgotPassword.setOnClickListener{
            val direction = AuthFragmentDirections.actionFragmentAuthToFragmentRecover()
            findNavController().navigate(direction)
        }
    }

    private fun observeAuthState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                authViewModel.authState.collect { state ->
                    handleAuthState(state)
                }
            }
        }
    }

    private fun handleAuthState(state: AuthViewModel.AuthState){
        when(state){
            is AuthViewModel.AuthState.Idle -> {} // ничего не делать
            is AuthViewModel.AuthState.Success -> {
                navigateToIntroduction()
            }
            is AuthViewModel.AuthState.ValidationError -> {
                showError(state.message)
            }
            is AuthViewModel.AuthState.Error -> {
                showError(state.message)
            }
        }
    }

    private fun navigateToIntroduction(){
        val direction = AuthFragmentDirections.actionFragmentAuthToFragmentIntroduction()
        findNavController().navigate(direction)
    }

    private fun showError(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun shouldShowMenu(): Boolean {
        return false
    }
}