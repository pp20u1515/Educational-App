package com.example.programmingc.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentRecoverBinding
import com.example.programmingc.presentation.additional_functions.checkValidEmail
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListener(){
        binding.buttonNext.setOnClickListener{
            val email = binding.email.text.toString().trim()

            if (checkValidEmail(email)){
                lifecycleScope.launch {
                    val rc = viewModel.resetPassword(email)

                    if (rc == true){
                        navigateDirection()
                    }
                    else {
                        Toast.makeText(requireContext(), "Failed to reset password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(requireContext(), "You entered your email incorrectly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateDirection(){
        val direction = RecoveryFragmentDirections.actionFragmentRecoverToFragmentAuth()
        findNavController().navigate(direction)
    }
}