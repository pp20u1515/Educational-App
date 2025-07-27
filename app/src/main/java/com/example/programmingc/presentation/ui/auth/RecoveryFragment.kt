package com.example.programmingc.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentRecoverBinding
import com.example.programmingc.presentation.additional_functions.checkValidEmail
import com.example.programmingc.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoveryFragment: Fragment() {
    private var _binding: FragmentRecoverBinding? = null
    private val binding: FragmentRecoverBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverBinding.inflate(inflater,container, false)

        binding.buttonNext.setOnClickListener{
            val email = binding.email.text.toString().trim()

            if (checkValidEmail(email)){
                val direction = RecoveryFragmentDirections.actionFragmentRecoverToFragmentSuccRecovery()
                findNavController().navigate(direction)
            }
            else{
                Toast.makeText(requireContext(), "Неправильно ввели почту", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}